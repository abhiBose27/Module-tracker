package fr.epita.assistant.tracker.service;

import fr.epita.assistant.tracker.Tracker;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyModulePlayer implements Tracker {

    private int bpm;

    private int tickLen;
    ArrayList<Channel> channelArrayList = new ArrayList<>();

    List<String[]> matrix;

    private List<String> getLines(String modulePath){
        List<String> lines = null;
        try (Stream<String> x = Files.lines(Paths.get(modulePath))) {
            lines = x.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
    @Override
    public void play(String modulePath, Supplier<OutputService> outputServiceSupplier) {

        List<String> lines = getLines(modulePath);
        this.bpm = Integer.parseInt(lines.get(2));
        this.tickLen = (1000 * 60) / (this.bpm * 4);
        this.matrix = new ArrayList<>();
        for (int i = 3; i < lines.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder(lines.get(i));
            stringBuilder.deleteCharAt(0);
            lines.set(i, stringBuilder.toString());
            String[] words = lines.get(i).split("\\|");
            matrix.add(words);
        }
        for (int i = 0; i < matrix.get(0).length; i++) {
            this.channelArrayList.add(new Channel(i, this.bpm, outputServiceSupplier));
        }

        for (int i = 0; i < matrix.size(); i++) {
            play_channel(matrix, i);
        }
    }


    public void play_channel(List<String[]> matrix, int index){
        var future = new ArrayList<CompletableFuture<Void>>();
        CompletableFuture<Void> tmp = null;

        for (int i = 0; i < channelArrayList.size(); i++) {
            Channel channel = channelArrayList.get(i);
            channel.outputService.startTick(index);
            Tick.parseString(channel, matrix, index);
            if (channel.noteOff)
                continue;
            var exec = CompletableFuture.delayedExecutor((long) this.tickLen * channel.duration,
                    TimeUnit.MILLISECONDS);
            tmp = CompletableFuture.runAsync(() -> {}, exec);
            future.add(CompletableFuture.runAsync(() -> channel.outputService.play(channel.note, channel.velocity,
                    this.tickLen * channel.duration)));
        }
        if (tmp != null){
            try{
                tmp.get();
            }
            catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        MyModulePlayer tracker = new MyModulePlayer();
        tracker.play("C:\\Users\\user\\Documents\\d4\\resources\\mario.track", SynthOutputService::new);
    }
}


