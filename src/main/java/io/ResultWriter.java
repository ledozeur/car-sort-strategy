package io;

import collection.CarArray;

public interface ResultWriter {
    boolean save(CarArray data, String path);

    boolean save(CarArray data);
}
