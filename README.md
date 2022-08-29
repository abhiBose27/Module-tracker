# Whats a Tracker?

A tracker is a piece of software that reads a succession of events, expressed as text, and transforms them into instructions sent to a sampling engine or a synthesizer to generate sound (and in the best of cases, music). Various tracker software have different syntax to suit their specificities, but usually, events are organised as tracks (or channels), placed side by side, with each line representing a step (or tick) which may contain data to play or not.

Here is an example

```|E-601317F|C-601317F
|.........|.........
|E-60131..|C-60131..
|.........|.........
|.......60|.........
|.........|===......
|E-60131..|C-60131..
|.........|.........
...
```

This could be understood as :

- Two channels.

- On tick 0, play an E natural 6 (a note, more on that later), with the first instrument and a velocity of 0x31 (49) and a volume of 0x7F (127) for the first channel, and a C natural 6 on the second one.

- Nothing happens on tick 1, for either channel.

- On tick 2, an E-6 is played on the first channel and a C-6 is played on the second one. The volume is not specified, so it’s kept at its previous value (7F).

- Nothing happens on tick 3.

- On tick 4, nothing happens on the second channel, but the volume of the first one is set to 0x60 (96), decreasing the volume without altering the note being played.

- On tick 5, the first channel is now clear, but on the second one, we see a special note === which means note off, this will end the previous note instantly.

- Then things continue on.
