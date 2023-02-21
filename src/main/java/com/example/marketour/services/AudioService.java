package com.example.marketour.services;

import com.example.marketour.model.entities.Audio;
import com.example.marketour.repositories.AudioRepository;
import org.springframework.stereotype.Service;

@Service
public class AudioService {

    private final AudioRepository audioRepository;

    public AudioService(AudioRepository audioRepository) {
        this.audioRepository = audioRepository;
    }

    public Audio addAudio(String audioDescription, byte[] data) {
        final var audio = new Audio();
        audio.setData(data);
        audio.setDescription(audioDescription);
        return audioRepository.save(audio);
    }

    public void updateAudio(Audio audio) {
        audioRepository.save(audio);
    }
}
