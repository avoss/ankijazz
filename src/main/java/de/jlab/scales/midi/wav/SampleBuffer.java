package de.jlab.scales.midi.wav;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

public class SampleBuffer {
  private short[] samples;
  private int position;
  private int samplingRate;
  private int blockSize;

  public SampleBuffer(int samplingRate) {
    this.samplingRate = samplingRate;
    this.blockSize = 60 * samplingRate;
    this.samples = new short[blockSize];
  }

  public void add(short[] data) {
    ensureCapacity(position + data.length);
    for (int i = 0; i < data.length; i++) {
      this.samples[position + i] += data[i];
    }
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int newPosition) {
    ensureCapacity(newPosition + 1);
    this.position = newPosition;
  }
  
  private void ensureCapacity(int minSize) {
    if (samples.length >= minSize)
      return;
    short[] temp = new short[samples.length + blockSize];
    System.arraycopy(samples, 0, temp, 0, samples.length);
    this.samples = temp;
  }

  public AudioInputStream toAudioInputStream() {
    ByteBuffer buffer = ByteBuffer.allocate(position * 2); // sizeof(short)
    for (int i = 0; i < position; i++) {
      buffer.putShort(samples[i]);
    }
    InputStream inputStream = new ByteArrayInputStream(buffer.array());
    AudioFormat audioFormat = new AudioFormat(samplingRate, 16, 1, true, buffer.order() == ByteOrder.BIG_ENDIAN);
    return new AudioInputStream(inputStream, audioFormat, position);
  }
  
}
