package fr.epita.assistant.tracker.service;

import fr.epita.assistant.tracker.Note;

import java.util.List;

public class Tick {

    public static void parseString(Channel chan, List<String[]> matrix, int i){
        String tmp;
        String line  = matrix.get(i)[chan.index];
        //System.out.println(line);
        if (line.length() != 9)
            System.exit(1);
        tmp = line.substring(0, 3);
        //System.out.println(tmp);
        if (tmp.equals("===")){
            chan.duration = 1;
            chan.velocity = 127;
            chan.noteOff = true;
            chan.outputService.noteOff();
        }
        else if (!(tmp.equals("..."))){
            //System.out.println(tmp);
            chan.noteOff = false;
            chan.duration = 1;
            tmp = tmp.replace('-', 'n');
            tmp = tmp.replace('#', 's');
            tmp = tmp.replace('b', 'f');
            // System.out.println(tmp);
            Note note = Note.valueOf(tmp);
            chan.note = note.midi;
            checkDuration(chan, matrix, i);
        }
        else
            chan.noteOff = true;
        tmp = line.substring(3, 5);
        if (!tmp.equals("..")) {
            chan.instrument = Integer.parseInt(tmp, 16);
            chan.outputService.setInstrument(chan.instrument);
        }
        tmp = line.substring(5, 7);
        if (!tmp.equals(".."))
            chan.velocity = Integer.parseInt(tmp, 16);
        tmp = line.substring(7, 9);
        if (!tmp.equals("..")) {
            chan.volume = Integer.parseInt(tmp, 16);
            chan.outputService.setVolume(chan.volume);
        }
    }

    public static void checkDuration(Channel chan, List<String[]> matrix, int index){
        String tmp;

        for (int i = index + 1; i < matrix.size(); i++) {
            String line = matrix.get(i)[chan.index];
            tmp = line.substring(0, 3);
            if (tmp.equals("...")){
                chan.duration += 1;
                continue;
            }
            return;
        }
    }
}
