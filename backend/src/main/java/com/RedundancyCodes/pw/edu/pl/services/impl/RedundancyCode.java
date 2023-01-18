package com.RedundancyCodes.pw.edu.pl.services.impl;

import com.RedundancyCodes.pw.edu.pl.domain.Configuration;
import com.RedundancyCodes.pw.edu.pl.domain.ConfigurationDTO;
import com.RedundancyCodes.pw.edu.pl.domain.Progress;
import com.RedundancyCodes.pw.edu.pl.domain.Result;
import com.RedundancyCodes.pw.edu.pl.services.RedundancyCodeAPI;
import com.RedundancyCodes.pw.edu.pl.services.WrapperInterface;
import com.RedundancyCodes.pw.edu.pl.services.impl.alters.NoiseProducer;
import com.RedundancyCodes.pw.edu.pl.services.impl.alters.Reformator;
import com.RedundancyCodes.pw.edu.pl.services.impl.checkers.DataChecker;
import com.RedundancyCodes.pw.edu.pl.services.impl.fileServices.PNGReader;
import com.RedundancyCodes.pw.edu.pl.services.impl.fileServices.PNGWriter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RedundancyCode implements RedundancyCodeAPI {

    private Configuration conf = null;
    private static final String UPLOAD_DIRECTORY = "images";

    private Path lastFileNameAndPath = null;

    private Configuration configuration = null;

    private Progress progress = new Progress();

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public boolean setOrgPicture(MultipartFile file) {
        try {
            lastFileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            File image = new File(lastFileNameAndPath.toString());
            image.createNewFile();
            Files.write(Path.of(lastFileNameAndPath.toString()), file.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public InputStream getProcPicture() throws FileNotFoundException {
        Path ResultFileNameAndPath = Paths.get(UPLOAD_DIRECTORY, "result.png");
        File sourceImage = new File(ResultFileNameAndPath.toUri());
        return new FileInputStream(sourceImage);
    }

    @Override
    public InputStream getOrgPicture() throws FileNotFoundException {
        File sourceImage = new File(lastFileNameAndPath.toUri());
        return new FileInputStream(sourceImage);
    }

    @Override
    public int getBitCount() {
        PNGReader reader = new PNGReader();
        int[][] valueMatrix = reader.readImageToIntegerArray(lastFileNameAndPath.toString());
        return valueMatrix.length * valueMatrix[0].length * 32;
    }

    @Override
    public Progress getProgress(){
        return progress;
    }



    @Override
    public Result getResult(ConfigurationDTO confDTO) {
        configuration = new Configuration();
        modelMapper.map(confDTO, configuration);
        progress.setState("Reading file");
        progress.setValue(0);
        PNGReader reader = new PNGReader();
        int[][] valueMatrix = reader.readImageToIntegerArray(lastFileNameAndPath.toString());
        progress.setState("Chunk reformation");
        progress.setValue(10);
        Reformator reformator = new Reformator(configuration.getChunkInfo().getChunkSize(), configuration.getChunkInfo().getBufferSize());
        StringBuilder[] bitMatrix = reformator.divide(reader.getWidth(), reader.getHeight(), valueMatrix);
        progress.setState("Encoding");
        progress.setValue(20);
        WrapperInterface wrapper = configuration.getRedundancyMethodProvider().getRedundancyMethod();
        StringBuilder[] encodedMatrix = wrapper.encode(bitMatrix);
        progress.setState("Introducing errors");
        progress.setValue(50);
        NoiseProducer noiseProducer = new NoiseProducer(configuration.getNoiseInfo().getNoiseBytesPerMillion());
        StringBuilder[] dirtyEncodedMatrix = noiseProducer.introduceNoise(encodedMatrix);
        progress.setState("Decoding");
        progress.setValue(70);
        StringBuilder[] decodedMatrix = wrapper.decode(dirtyEncodedMatrix);
        progress.setState("Verifying with original");
        progress.setValue(80);
        DataChecker dt = new DataChecker();
        dt.checkWithOriginal(wrapper.getChunkToResendSet(), noiseProducer.getContaminatedChunkSet());
        Result result = new Result();
        result.setFn(dt.getFNBrokenChunks().size());
        result.setFp(dt.getFPBrokenChunks().size());
        result.setTp(dt.getTPBrokenChunks().size());
        result.setTn(noiseProducer.getCleanChunkSet().size());
        progress.setState("Writing to File");
        progress.setValue(90);
        String bitString = reformator.extractEsseneAndMergeString(decodedMatrix);
        int[] integerResultArray = reformator.mergeToOneIntegerArray(reader.getWidth(), reader.getHeight(), bitString);
        PNGWriter writer = new PNGWriter(reader.getWidth(), reader.getHeight());
        writer.convertToFile(integerResultArray, UPLOAD_DIRECTORY + "/result.png");
        progress.setState("Done");
        progress.setValue(100);
        return result;
    }
}
