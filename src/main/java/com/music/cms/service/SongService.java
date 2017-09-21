package com.music.cms.service;

import com.music.cms.model.Song;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SongService {
    List<Song> listAll();
    Song findById(Integer id);
    void store(Song song);
    void update(Song song);
    void destroy(Integer id);
}
