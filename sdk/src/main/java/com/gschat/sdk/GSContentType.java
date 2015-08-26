package com.gschat.sdk;


/**
 * gs message content type
 */
public enum GSContentType {

    Null(0),Text(1), Image(2), Video(3), Location(4), Voice(5), File(6), Command(7);

    private int value;

    GSContentType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static GSContentType create(int value) {
        switch (value) {
            case 1:
                return Text;
            case 2:
                return Image;
            case 3:
                return Video;
            case 4:
                return Location;
            case 5:
                return Voice;
            case 6:
                return File;
            case 7:
                return Command;
            default:
                return Text;

        }
    }
}
