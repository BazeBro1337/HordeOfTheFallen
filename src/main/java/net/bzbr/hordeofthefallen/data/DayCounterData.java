package net.bzbr.hordeofthefallen.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

public class DayCounterData extends PersistentState {

    private int dayCounter;

    // Конструктор
    public DayCounterData() {
        this.dayCounter = 1;  // Начальное значение
    }

    // Метод для обновления счётчика дней
    public void setDayCounter(int day) {
        this.dayCounter = day;
        this.markDirty();  // Помечаем данные для сохранения
    }

    public void incrementDayCounter() {
        this.dayCounter++;
        this.markDirty();  // Помечаем данные для сохранения
    }

    public int getDayCounter() {
        return this.dayCounter;
    }

    // Сохранение данных
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("DayCounter", this.dayCounter);
        return nbt;
    }

    // Загрузка данных
    public static DayCounterData fromNbt(NbtCompound nbt) {
        DayCounterData data = new DayCounterData();
        data.dayCounter = nbt.getInt("DayCounter");
        return data;
    }
}
