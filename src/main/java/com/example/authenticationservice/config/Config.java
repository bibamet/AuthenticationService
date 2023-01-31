package com.example.authenticationservice.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class Config {

    @Bean
    public PrivateKey privateAccessKey(@Value("${internal.keys.private.access-path}") String path) throws IOException, ClassNotFoundException {
        ObjectInputStream oisprivate = new ObjectInputStream(new FileInputStream(Paths.get(path).toFile()));
        return (PrivateKey) oisprivate.readObject();
    }

    @Bean
    public PublicKey publicAccessKey(@Value("${internal.keys.public.access-path}") String path) throws IOException, ClassNotFoundException {
        ObjectInputStream oispublic = new ObjectInputStream(new FileInputStream(Paths.get(path).toFile()));
        return (PublicKey) oispublic.readObject();
    }

    @Bean
    public PrivateKey privateRefreshKey(@Value("${internal.keys.private.refresh-path}") String path) throws IOException, ClassNotFoundException {
        ObjectInputStream oisprivate = new ObjectInputStream(new FileInputStream(Paths.get(path).toFile()));
        return (PrivateKey) oisprivate.readObject();
    }

    @Bean
    public PublicKey publicRefreshKey(@Value("${internal.keys.public.refresh-path}") String path) throws IOException, ClassNotFoundException {
        ObjectInputStream oispublic = new ObjectInputStream(new FileInputStream(Paths.get(path).toFile()));
        return (PublicKey) oispublic.readObject();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder().create();
    }

//    @PostConstruct
    public void keyPair() throws FileNotFoundException, IOException {

        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

        try (ObjectOutputStream ouspublic = new ObjectOutputStream(new FileOutputStream(Paths.get("src/main/resources/publicrefreshkey.cer").toFile()))) {
            ouspublic.writeObject(keyPair.getPublic());
        }

        try (ObjectOutputStream ousprivate = new ObjectOutputStream(new FileOutputStream(Paths.get("src/main/resources/privaterefreshkey.cer").toFile()))) {
            ousprivate.writeObject(keyPair.getPrivate());
        }

    }

}
