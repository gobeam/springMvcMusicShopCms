package com.music.cms.service;

import com.music.cms.dao.SongDao;
import com.music.cms.model.Song;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@PropertySource(value = { "classpath:application.properties" })
public class SongServiceImpl implements SongService {

    @Autowired
    private Environment environment;

    @Autowired
    private SongDao songDao;

    @Override
    public List<Song> listAll() {
        return songDao.listAll();
    }

    @Override
    public Song findById(Integer id) {
        return songDao.findById(id);
    }

    @Override
    public void store(Song song) {
        if (!song.getFile().isEmpty()) {
            String filename = uploadImage(song.getFile());
            song.setImage(filename);
        }
        songDao.store(song);
    }

    /**
     *
     * @param file
     * @return
     */
    private String uploadImage(MultipartFile file)
    {
        try {

            // Get the file and save it somewhere
//                byte[] bytes = file.getBytes();
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = generateUniqueFileName() + "." +ext;
            String nameAndPath = environment.getRequiredProperty("image.filePath") + fileName;
//                Path path = Paths.get(UPLOADED_FOLDER + fileName);
//                Files.write(path, bytes);
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            BufferedImage resizeImageJpg = cropImage(originalImage,350,286, type);

            File outputFile = new File(nameAndPath);
            outputFile.getParentFile().mkdirs();
            ImageIO.write(resizeImageJpg, ext, outputFile);
            return fileName;

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @param originalImage
     * @param width
     * @param height
     * @param type
     * @return
     */
    private BufferedImage cropImage(BufferedImage originalImage,int width ,int height, int type) {
        BufferedImage resizedImage = new BufferedImage( width, height, type);//set width and height of image
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }


    /**
     *
     * @return
     */
    private String generateUniqueFileName() {
        String C36UUID = UUID.randomUUID().toString();

        String C16 = C36UUID.replaceAll("-", "");

        return C16.substring(0, 16);
    }

    @Override
    public void update(Song song) {
        if (!song.getFile().isEmpty()) {
            deleteImage(song.getImage());
            String filename = uploadImage(song.getFile());
            song.setImage(filename);
        }
        songDao.update(song);
    }


    /**
     *
     * @param imageName
     */
    private void deleteImage(String imageName)
    {
        try{
        File image = new File(environment.getRequiredProperty("image.filePath") + imageName);
        image.delete();
        }
        catch(Exception e)
        {
            System.out.println("Failed to Delete image !!");
        }

    }

    @Override
    public void destroy(Integer id) {
        songDao.destroy(id);
    }
}
