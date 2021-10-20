#include <linux/build-salt.h>
#include <linux/module.h>
#include <linux/vermagic.h>
#include <linux/compiler.h>

BUILD_SALT;

MODULE_INFO(vermagic, VERMAGIC_STRING);
MODULE_INFO(name, KBUILD_MODNAME);

__visible struct module __this_module
__section(.gnu.linkonce.this_module) = {
	.name = KBUILD_MODNAME,
	.init = init_module,
#ifdef CONFIG_MODULE_UNLOAD
	.exit = cleanup_module,
#endif
	.arch = MODULE_ARCH_INIT,
};

#ifdef CONFIG_RETPOLINE
MODULE_INFO(retpoline, "Y");
#endif

static const struct modversion_info ____versions[]
__used __section(__versions) = {
	{ 0xc79d2779, "module_layout" },
	{ 0xdecd0b29, "__stack_chk_fail" },
	{ 0xdb760f52, "__kfifo_free" },
	{ 0x8e17b3ae, "idr_destroy" },
	{ 0x7665a95b, "idr_remove" },
	{ 0x13d0adf7, "__kfifo_out" },
	{ 0x20978fb9, "idr_find" },
	{ 0xb8f11603, "idr_alloc" },
	{ 0xf23fcb99, "__kfifo_in" },
	{ 0xc5850110, "printk" },
	{ 0x139f2189, "__kfifo_alloc" },
	{ 0xbdfb6dbb, "__fentry__" },
};

MODULE_INFO(depends, "");


MODULE_INFO(srcversion, "4D50F902C5BDF3319FFF05D");
