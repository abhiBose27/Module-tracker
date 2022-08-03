package fr.epita.assistant.tracker.service;

import java.util.function.Supplier;

public class Channel {
    protected int note;
    protected int instrument;
    public OutputService outputService;
    protected int index;
    protected int duration = 1;
    protected int volume = 127;
    protected int velocity = 127;
    protected boolean noteOff = true;

    public Channel(int index, int bpm, Supplier<OutputService> outputServiceSupplier){
        this.index = index;
        this.outputService = outputServiceSupplier.get();
        this.outputService.init(index, bpm);
    }
}
