package net.mehvahdjukaar.supplementaries.common.world.songs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.mehvahdjukaar.supplementaries.common.capabilities.mob_container.BuiltinAnimation;
import net.mehvahdjukaar.supplementaries.common.capabilities.mob_container.DataDefinedCatchableMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Song {

    public static final Song EMPTY = new Song("empty", 1, List.of(), "", 1);

    public static final Codec<Song> CODEC = RecordCodecBuilder.<Song>create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(p -> p.name),
            Codec.intRange(1,1000).optionalFieldOf("tempo",1).forGetter(p -> p.tempo),
            Codec.INT.listOf().fieldOf("notes").forGetter(p -> List.of(p.notes)),
            Codec.STRING.optionalFieldOf("credits","").forGetter(p->p.credits),
            Codec.intRange(0, 10000).optionalFieldOf("weight", 100).forGetter(p -> p.weight)
    ).apply(instance, Song::new)).comapFlatMap((s) -> {
        if (s.notes.length <= 0)
            return DataResult.error("Song note list cant be empty");
        return DataResult.success(s);
    }, Function.identity());

  //  public static final Codec<Song> CLIENT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
  //          Codec.STRING.fieldOf("name").forGetter(p -> p.name),
  //          Codec.STRING.optionalFieldOf("credits","").forGetter(p->p.credits)
  //  ).apply(instance, Song::new));


    private final String name;
    private final int tempo;
    private Integer[] notes;
    private final String credits;
    private final int weight;

    private boolean processed = false;

    public Song(String name, int tempo, List<Integer> notes, String credits, int weight) {
        this.name = name;
        this.tempo = Math.max(1, tempo);
        this.notes = notes.toArray(new Integer[0]);
        this.credits = credits;
        this.weight = weight;
    }

    public boolean isValid(){
        return this.processed;
    }

    //makes it usable to be played
    private void processForPlaying() {
        List<Integer> newNotes = new ArrayList<>();
        for (int i : notes) {
            if (i <= 0) {
                int j = -Math.min(-1, i);
                //-1 and 0 are the same
                int blanks = j - 1;

                for (int k = 0; k < blanks; k++) {
                    newNotes.add(0);
                }
            } else newNotes.add(i);
        }
        this.notes = newNotes.toArray(new Integer[0]);
    }

    public String getTranslationKey() {
        return name;
    }

    public int getTempo() {
        return  Math.max(1,tempo);
    }

    public Integer[] getNotes() {
        return notes;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Song: "+ name;
    }

    public int getWeight() {
        return weight;
    }


    public IntList getNoteToPlay(long timeSinceStarted) {
        IntList toPlay = new IntArrayList();

        try {
            int currentIndex = (int) (timeSinceStarted / this.getTempo()) % this.notes.length;
            int n = notes[currentIndex];
            while (n > 1) {
                toPlay.add(Mth.clamp(n % 100, 0, 25));
                n = n / 100;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toPlay;
    }

    public void validatePlayReady() {
        if(!this.processed){
            processForPlaying();
            this.processed = true;
        }
    }


}