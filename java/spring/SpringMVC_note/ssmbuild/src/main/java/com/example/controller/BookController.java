package com.example.controller;

import com.example.entity.Books;
import com.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;


    /**
     * 查询所有书籍并返回展示页面
     * @param model
     * @return
     */
    @GetMapping("/allBook")
    public String list(Model model) {
        List<Books> books = bookService.findAll();
        model.addAttribute("books", books);
        return "all_book";
    }

    /**
     * 添加书籍并返回到所有书籍页
     * @param bookName
     * @param bookCounts
     * @param detail
     * @param model
     * @return
     */
    @PostMapping("/addBook")
    public String add(@RequestParam("bookName") String bookName,
                      @RequestParam("bookCounts") int bookCounts,
                      @RequestParam("detail") String detail,
                      Model model) {
        Books books = new Books();
        books.setBookName(bookName);
        books.setBookCounts(bookCounts);
        books.setDetail(detail);
        bookService.addBook(books);

        return "redirect: /ssmbuild/book/allBook";
    }

    /**
     * 根据ID删除书籍
     * @param bookId
     * @return
     */
    @GetMapping("/deleteBook")
    public String delete(@RequestParam("bookId") int bookId) {
        bookService.deleteBookById(bookId);
        return "redirect: /ssmbuild/book/allBook";
    }

    /**
     * 根据ID修改书籍
     * @param bookId
     * @param model
     * @return
     */
    @GetMapping("/updateBook")
    public String update(@RequestParam("bookId") int bookId,
                         Model model) {
        Books books = bookService.queryById(bookId);
        model.addAttribute("book", books);
        return "update";
    }

    /**
     * 处理修改书籍
     * @param bookId
     * @param bookName
     * @param bookCounts
     * @param detail
     * @return
     */
    @PostMapping("/updateBook")
    public String updateBook(@RequestParam("bookId") int bookId,
                             @RequestParam("bookName") String bookName,
                             @RequestParam("bookCounts") int bookCounts,
                             @RequestParam("detail") String detail) {

        Books books = new Books();
        books.setBookId(bookId);
        books.setBookName(bookName);
        books.setBookCounts(bookCounts);
        books.setDetail(detail);
        bookService.updateBook(books);
        return "redirect: /ssmbuild/book/allBook";
    }

}
