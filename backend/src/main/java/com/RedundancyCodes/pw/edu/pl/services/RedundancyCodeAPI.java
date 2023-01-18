package com.RedundancyCodes.pw.edu.pl.services;

import com.RedundancyCodes.pw.edu.pl.domain.ChunkInfo;
import com.RedundancyCodes.pw.edu.pl.domain.ConfigurationDTO;
import com.RedundancyCodes.pw.edu.pl.domain.Progress;
import com.RedundancyCodes.pw.edu.pl.domain.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface RedundancyCodeAPI {

    public boolean setOrgPicture(MultipartFile file) throws IOException;

    public InputStream getOrgPicture() throws FileNotFoundException;

    public int getBitCount();

    public InputStream getProcPicture() throws FileNotFoundException;

    public Progress getProgress();

    public Result getResult(ConfigurationDTO confDTO);
}
