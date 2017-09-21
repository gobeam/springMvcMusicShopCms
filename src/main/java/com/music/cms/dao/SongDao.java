package com.music.cms.dao;

import com.music.cms.model.Song;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface SongDao {
    List<Song> listAll();
    Song findById(Integer id);
    void store(Song song);
    void update(Song song);
    void destroy(Integer id);
}
