ifneq ($(KERNELRELEASE), )
obj-m := helloDev.o
else
PWD := $(shell pwd)
KDIR := /lib/modules/5.4.0-74-generic/build
all:
	make -C $(KDIR) M=$(PWD)
clean:
	rm -rf *.o *.ko *.mod.c *.symvers *.c~ *~
endif
