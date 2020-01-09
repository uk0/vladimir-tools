# $Id$

outdir = ../bin

targets = $(outdir)/libINotify.so

ARCH = linux

JAVA_JDK=/usr/lib/jvm/java-8-openjdk-amd64

CFLAGS=-I${JAVA_JDK}/include -I${JAVA_JDK}/include/${ARCH} -fPIC # -DDEBUG=1

.SUFFIXES: all clean

all: $(targets)
clean:
	$(RM) $(targets) $(foreach i, $(targets), $($(i)_objs))

$(outdir)/libINotify.so_objs = INotify.o # fprintbuf.o # <--- use only if -DDEBUG=1 in CFLAGS
$(outdir)/libINotify.so_libs =
$(outdir)/libINotify.so: $($(outdir)/libINotify.so_objs)
	$(CC) $(LDFLAGS) -shared -o $@ $($@_objs) $($@_ldflags) $($@_libs)

INotify.o: utils_linux_inotify_INotify.h

utils_linux_inotify_INotify.h: utils/linux/inotify/INotify.java
	javah utils.linux.inotify.INotify

fprintbuf.o: fprintbuf.h
