package com.RedundancyCodes.pw.edu.pl.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChunkInfo {

    private int chunkSize;

    private int bufferSize;

    private int chunkNumber;
}
