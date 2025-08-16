/*
 *  Copyright 2023 The original authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package dev.morling.onebrc;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Helper {

    // this is useless now, because this will target to specific data set only
    private static List<long[]> getStartAndEndOffset(int numChunks, String filePath) throws IOException {
        Path filePaths = Path.of(filePath);

        long fileSize = Files.size(filePaths);
        long chunkSize = fileSize / numChunks;

        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r");
                FileChannel channel = raf.getChannel()) {

            long[] startOffsets = new long[numChunks];
            long[] endOffsets = new long[numChunks];

            long start = 0;
            for (int i = 0; i < numChunks; i++) {
                startOffsets[i] = start;

                if (i == numChunks - 1) {
                    // Last chunk goes to end of file
                    endOffsets[i] = fileSize;
                    break;
                }

                long tentativeEnd = start + chunkSize;
                channel.position(tentativeEnd);

                // move forward until we find a newline
                int b;
                do {
                    b = raf.read();
                    tentativeEnd++;
                } while (b != -1 && b != '\n');

                endOffsets[i] = tentativeEnd;
                start = tentativeEnd;
            }

            // Print chunk byte ranges
            for (int i = 0; i < numChunks; i++) {
                System.out.printf("Chunk %d: start=%d, end=%d, size=%d%n",
                        i, startOffsets[i], endOffsets[i], (endOffsets[i] - startOffsets[i]));
            }

            return List.of(startOffsets, endOffsets);
        }
    }

    static void main() throws IOException {
        String filePath = "./measurements.txt";
        long fileSize = Files.size(Path.of(filePath));
        System.out.println(fileSize);
        // bytes 13795555063

        getStartAndEndOffset(8, filePath);
    }
}
