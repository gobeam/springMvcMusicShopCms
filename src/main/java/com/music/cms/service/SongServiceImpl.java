package com.music.cms.service;

import com.music.cms.dao.SongDao;
import com.music.cms.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {

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
        songDao.store(song);
    }

    @Override
    public void update(Song song) {
        songDao.update(song);
    }

    @Override
    public void destroy(Integer id) {
        songDao.destroy(id);
    }
}
