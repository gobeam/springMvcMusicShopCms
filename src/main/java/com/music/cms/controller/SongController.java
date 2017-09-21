package com.music.cms.controller;

import com.music.cms.FlashMessage;
import com.music.cms.model.Song;
import com.music.cms.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/song")
public class SongController {

    @Autowired
    private SongService songService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model)
    {
        List<Song> songs = songService.listAll();
        model.addAttribute("songs",songs);
        model.addAttribute("pageTitle","Song Management");
        model.addAttribute("Createbutton","Add Song");
        return "backend/song/index";

    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(ModelMap model)
    {
        if(!model.containsAttribute("song"))
        {
            model.addAttribute("song",new Song());
        }
        model.addAttribute("button","Add");
        model.addAttribute("pageTitle","Add Song");
        model.addAttribute("url",String.format("/admin/song/store"));
        return "backend/song/form";

    }

    @RequestMapping(value = "/store", method = RequestMethod.POST)
    public String store(@Valid @ModelAttribute("song") Song song, BindingResult result, RedirectAttributes redirect) throws Exception
    {
        if(result.hasErrors())
        {
            redirect.addFlashAttribute("org.springframework.validation.BindingResult.song",result);
            redirect.addFlashAttribute("song",song);
            return "redirect:/admin/song/create";
        }

        songService.store(song);
        redirect.addFlashAttribute("flash",new FlashMessage("Song added successfully!",FlashMessage.Status.SUCCESS));
        return "redirect:/admin/song";
    }


    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Integer id, ModelMap model,RedirectAttributes redirect)
    {
        Song song = songService.findById(id);

        if (song == null) {
            redirect.addFlashAttribute("flash", new FlashMessage("Song not found!", FlashMessage.Status.DANGER));
            return "redirect:/admin/song";
        }


        if(!model.containsAttribute("song"))
        {
            model.addAttribute("song",song);
        }
        model.addAttribute("button","Update");
        model.addAttribute("pageTitle","Update Songs");
        model.addAttribute("url",String.format("/admin/song/%s/update",id));
        return "backend/song/form";

    }

    @RequestMapping(value = "/{id}/update" , method = RequestMethod.POST)
    public String update(@PathVariable("id") Integer id,@ModelAttribute("song") Song song, BindingResult result, RedirectAttributes redirect)throws Exception
    {
        if(result.hasErrors())
        {
            redirect.addFlashAttribute("org.springframework.validation.BindingResult.song",result);
            redirect.addFlashAttribute("song",song);
            return String.format("redirect:/admin/song/%s/edit",id);
        }
        songService.update(song);
        redirect.addFlashAttribute("flash",new FlashMessage("Song updated successfully!",FlashMessage.Status.SUCCESS));
        return "redirect:/admin/song";
    }

    @RequestMapping(value = "/{id}/delete",method = RequestMethod.POST)
    public String destroy(@PathVariable("id") Integer id, RedirectAttributes redirect)
    {
        Song song = songService.findById(id);

        if (song == null) {
            redirect.addFlashAttribute("flash", new FlashMessage("Data not found!", FlashMessage.Status.DANGER));
            return "redirect:/admin/song";
        }
        songService.destroy(id);
        redirect.addFlashAttribute("flash",new FlashMessage("Song deleted successfully!",FlashMessage.Status.SUCCESS));
        return "redirect:/admin/song";

    }



}
