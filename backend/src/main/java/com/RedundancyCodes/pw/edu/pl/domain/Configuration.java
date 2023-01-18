package com.RedundancyCodes.pw.edu.pl.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

    private RedundancyMethodProvider redundancyMethodProvider;

    private ChunkInfo chunkInfo;

    private NoiseInfo noiseInfo;

}
