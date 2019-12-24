/*
 *    Copyright 2013 Matteo Catena (catena.matteo@gmail.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package utils.io.directio;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import java.io.IOException;
import java.io.OutputStream;

public class JNAOutputStream extends OutputStream {

	//just a couple of them are used, here for eventual future uses
	protected static final int O_RDONLY = 00;
	protected static final int O_WRONLY = 01;
	protected static final int O_RDWR = 02;
	protected static final int O_CREAT = 0100;
	protected static final int O_EXCL = 0200;
	protected static final int O_NOCTTY = 0400;
	protected static final int O_TRUNC = 01000;
	protected static final int O_APPEND = 02000;
	protected static final int O_NONBLOCK = 04000;
	protected static final int O_NDELAY = O_NONBLOCK;
	protected static final int O_SYNC = 010000;
	protected static final int O_ASYNC = 020000;
	protected static final int O_DIRECT = 040000;
	protected static final int O_DIRECTORY = 0200000;
	protected static final int O_NOFOLLOW = 0400000;
	protected static final int O_NOATIME = 01000000;
	protected static final int O_CLOEXEC = 02000000;
	
	//fcntl has also other flags, not presented here
	protected static final int F_SETFL = 4;
	   	
	public static final int BLOCK_SIZE = 512;    
  
	static {
		Native.register("c");
	}    
  
	private native int open(String pathname, int flags, int mode);
	private native int fcntl(int id, int cmd, int flags);
	private native int write(int fd, Pointer buf, int count);
	private native int posix_memalign(PointerByReference memptr, int alignment, int size);
	private native int close(int fd);
  
	private int fd;
	private Pointer bufPnt;
	private byte[] buffer;
	private int pos;
  
	public JNAOutputStream(String pathname, boolean direct) throws IOException {
  			
		if (!direct) {
			fd = open(pathname, O_CREAT|O_APPEND|O_WRONLY, 00755);
//			fd = open(pathname, O_CREAT|O_TRUNC|O_WRONLY, 00755);
		} else {
//			fd = open(pathname, O_CREAT|O_TRUNC|O_WRONLY|O_DIRECT, 00755);
			fd = open(pathname, O_CREAT|O_APPEND|O_WRONLY|O_DIRECT, 00755);
		}

		PointerByReference pntByRef = new PointerByReference();
		posix_memalign(pntByRef, BLOCK_SIZE, BLOCK_SIZE);
		bufPnt = pntByRef.getValue();    	
		buffer = new byte[BLOCK_SIZE];
	}
			
	@Override
	public void close() throws IOException {
		
		if (pos % BLOCK_SIZE != 0) {
			if (fcntl(fd, F_SETFL, ~O_DIRECT) < 0) {
				throw new IOException("Impossible to modify fd on close()");
			}
			bufPnt.write(0, buffer, 0, pos);
			int rtn = write(fd,bufPnt,pos);
			if (rtn != pos) {
				throw new IOException(rtn+"/"+pos+" bytes written to disk on close()");
			}
		}
		if (close(fd)< 0) {
			throw new IOException("Problems occured while doing close()");
		}
	}	
	
	@Override
	public void write(int b) throws IOException {
		if (pos < BLOCK_SIZE) {
			buffer[pos++] = (byte)b;
		}
		if (pos == BLOCK_SIZE) {
			bufPnt.write(0, buffer, 0, BLOCK_SIZE);
			int rtn = write(fd, bufPnt, BLOCK_SIZE);
			if (rtn != BLOCK_SIZE) {
				throw new IOException(rtn+"/"+BLOCK_SIZE+" bytes written to disk");
			}
			pos = 0;
		}
	}

}
