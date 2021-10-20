ifneq ($(KERNELRELEASE), )
obj-m := helloDri.o
else
PWD := $(shell pwd)
KERNELDIR := /lib/modules/`uname -r`/build
all:
	make -C $(KERNELDIR) M=$(PWD) modules
clean:
	rm -rf *.o *.ko *.mod.c *.symvers *.c~ *~
endif

