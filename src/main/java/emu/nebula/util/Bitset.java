package emu.nebula.util;

import lombok.Getter;

@Getter
public class Bitset {
    private long[] data;
    
    public Bitset() {
        this.data = new long[1];
    }
    
    public Bitset(long[] longArray) {
        this.data = longArray;
    }

    public boolean isEmpty() {
        return this.data.length == 1 && this.data[0] == 0L;
    }
    
    public void clear() {
        this.data = new long[1];
    }
    
    public boolean isSet(int index) {
        int longArrayOffset = (int) Math.floor((index - 1) / 64D);
        int bytePosition = ((index - 1) % 64);
        
        if (longArrayOffset >= this.data.length) {
            return false;
        }
        
        long flag = 1L << bytePosition;
        return (this.data[longArrayOffset] & flag) == flag;
    }

    public void setBit(int index) {
        int longArrayOffset = (int) Math.floor((index - 1) / 64D);
        int bytePosition = ((index - 1) % 64);
        
        if (longArrayOffset >= this.data.length) {
            var oldData = this.data;
            this.data = new long[longArrayOffset + 1];
            System.arraycopy(oldData, 0, this.data, 0, oldData.length);
        }
        
        this.data[longArrayOffset] |= (1L << bytePosition);
    }
    
    public void unsetBit(int index) {
        int longArrayOffset = (int) Math.floor((index - 1) / 64D);
        int bytePosition = ((index - 1) % 64);
        
        if (longArrayOffset >= this.data.length) {
            var oldData = this.data;
            this.data = new long[longArrayOffset + 1];
            System.arraycopy(oldData, 0, this.data, 0, oldData.length);
        }
        
        this.data[longArrayOffset] &= ~(1L << bytePosition);
    }
    
    public byte[] toByteArray() {
        byte[] array = new byte[this.getData().length * 8];

        for (int i = 0; i < this.getData().length; i++) {
            long value = this.getData()[i];
            
            for (int x = 7; x >= 0; x--) {
                array[(i * 8) + x] = (byte) (value & 0xFF);
                value >>= Byte.SIZE;
            }
        }
        
        return array;
    }
    
    public byte[] toBigEndianByteArray() {
        byte[] array = new byte[this.getData().length * 8];

        for (int i = 0; i < this.getData().length; i++) {
            long value = this.getData()[i];
            
            for (int x = 0; x <= 7; x++) {
                array[(i * 8) + x] = (byte) (value & 0xFF);
                value >>= Byte.SIZE;
            }
        }
        
        return array;
    }
}
