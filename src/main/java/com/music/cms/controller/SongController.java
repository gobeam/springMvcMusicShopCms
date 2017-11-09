package com.music.cms.controller;

import com.music.cms.FlashMessage;
import com.music.cms.model.Category;
import com.music.cms.model.Song;
import com.music.cms.service.SongService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping(value = "/admin/song")
public class SongController {

    @Autowired
    private SongService songService;

    private static String UPLOADED_FOLDER = "/home/alis/mytemp/";


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
    public String store(@Valid @ModelAttribute("song") Song song, BindingResult result, RedirectAttributes redirect,HttpServletRequest request) throws Exception
    {
        if(result.hasErrors())
        {
            redirect.addFlashAttribute("org.springframework.validation.BindingResult.song",result);
            redirect.addFlashAttribute("song",song);
            return "redirect:/admin/song/create";
        }

        if (!song.getFile().isEmpty()) {
            try {
                MultipartFile file  = song.getFile();
                // Get the file and save it somewhere
//                byte[] bytes = file.getBytes();
                String ext = FilenameUtils.getExtension(file.getOriginalFilename());
                String nameAndPath = UPLOADED_FOLDER + generateUniqueFileName()+"."+ext;
//                Path path = Paths.get(UPLOADED_FOLDER + fileName);
//                Files.write(path, bytes);

                BufferedImage originalImage = ImageIO.read(file.getInputStream());
                int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
                BufferedImage resizeImageJpg = cropImage(originalImage,20,20, type);

                File outputFile = new File(nameAndPath);
                outputFile.getParentFile().mkdirs();
                ImageIO.write(resizeImageJpg, ext, outputFile);

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        songService.store(song);
        redirect.addFlashAttribute("flash",new FlashMessage("Song added successfully!",FlashMessage.Status.SUCCESS));
        return "redirect:/admin/song";
    }



    private BufferedImage cropImage(BufferedImage originalImage,int width ,int height, int type) {
        BufferedImage resizedImage = new BufferedImage( width, height, type);//set width and height of image
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }


    private String generateUniqueFileName() {
        String C36UUID = UUID.randomUUID().toString();

        String C16 = C36UUID.replaceAll("-", "");

        return C16.substring(0, 16);
    }




    public String uploadImage(MultipartFile file,HttpServletRequest request)
    {
//        try {
//            String fileName = null;
//            InputStream inputStream = null;
//            OutputStream outputStream = null;
//
//            System.out.println("size::" + file.getSize());
//            fileName = request.getSession().getServletContext().getRealPath("/") + "/images/"
//                    + file.getOriginalFilename();
//            outputStream = new FileOutputStream(fileName);
//           // System.out.println("fileName:" + file.getOriginalFilename());
//
//            int readBytes = 0;
//            byte[] buffer = new byte[10000];
//            while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
//                outputStream.write(buffer, 0, readBytes);
//            }
//            outputStream.close();
//            inputStream.close();
//        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        } catch (Exception e) {
//        e.printStackTrace();
//    }



        return null;
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
