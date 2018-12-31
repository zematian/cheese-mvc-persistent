package org.launchcode.controllers;





import org.launchcode.models.Cheese;

import org.launchcode.models.Menu;

import org.launchcode.models.data.CheeseDao;

import org.launchcode.models.data.MenuDao;

import org.launchcode.models.forms.AddMenuItemForm;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.Errors;

import org.springframework.web.bind.annotation.*;



import javax.validation.Valid;



@Controller

@RequestMapping("menu")

public class MenuController {



    @Autowired

    private MenuDao menuDao;



    @Autowired

    private CheeseDao cheeseDao;



    @RequestMapping(value = "")

    public String index(Model model){



        model.addAttribute("title","Menus");

        model.addAttribute("menus", menuDao.findAll());

        return "menu/index";



    }



    @RequestMapping(value = "add", method = RequestMethod.GET)

    public String displayAddMenuForm(Model model){

        model.addAttribute("title", "Add menu");

        model.addAttribute("menu", new Menu());



        return "menu/add";

    }



    @RequestMapping(value = "add", method = RequestMethod.POST)

    public String processAddMenuForm(Model model, @ModelAttribute @Valid Menu menu, Errors errors){



        if(errors.hasErrors()){

            model.addAttribute("title", "Add menu");

            model.addAttribute("menu", menu);

            return "menu/add";

        }



        menuDao.save(menu);

        return "redirect:view/" + menu.getId();

    }



    @RequestMapping(value="view/{menuId}", method = RequestMethod.GET)

    public String viewMenu(Model model, @PathVariable int menuId){

        Menu menu = menuDao.findOne(menuId);

        model.addAttribute("menu", menu);

        model.addAttribute("title", menu.getName());

        model.addAttribute("cheeses", menu.getCheeses());

        model.addAttribute("menuId", menu.getId());



        return "menu/view";

    }



    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)

    public String displayAddItemForm(Model model, @PathVariable int menuId){

        Menu menu =menuDao.findOne(menuId);

        //Cheese cheeses = cheeseDao.findAll();

        AddMenuItemForm form = new AddMenuItemForm(menu,cheeseDao.findAll() );

        model.addAttribute("title", "Add item to menu:" + menu.getName() );

        model.addAttribute("form", form);

        return "menu/add-item";

    }



    @RequestMapping(value = "add-item", method = RequestMethod.POST)

    public String processAddItem(Model model, @ModelAttribute @Valid AddMenuItemForm form,

                                 @RequestParam int cheeseId, @RequestParam int menuId, Errors errors){

        if (errors.hasErrors()){

            model.addAttribute("title", "Add item to menu:" + form.getMenu().getName() );

            model.addAttribute("form", form);

            return "menu/add-item";

        }



        //Cheese cheese = cheeseDao.findAllById(cheeseId);

        Cheese theCheese = cheeseDao.findOne(form.getCheeseId());

        Menu theMenu = menuDao.findOne(form.getMenuId());

        theMenu.addItem(theCheese);

        menuDao.save(theMenu);

        return "redirect:view/" + theMenu.getId();

    }



}