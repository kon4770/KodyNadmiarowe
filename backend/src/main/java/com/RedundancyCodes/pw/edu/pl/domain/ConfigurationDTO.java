package com.RedundancyCodes.pw.edu.pl.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationDTO {

    private RedundancyMethodProvider redundancyMethodProvider;

    private ChunkInfo chunkInfo;

    private NoiseInfo noiseInfo;
}
