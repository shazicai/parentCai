package cn.com.springCai.util;

import org.springframework.stereotype.Component;

/**
 * @Author caiJH
 * @Date 2025/3/3 9:54 AM
 * @Version 1.0
 */
@Component
public class SnowflakeWorkId {

    /**
     * 初始时间
     */
    private final long startTime = 1740967027836L;

    /**
     * 机房占位
     */
    private long machineRoomIdBits = 5L;

    /**
     * 服务器号占位
     */
    private long computerIdBits = 5L;

    /**
     * 序号占位
     */
    private long sequenceIdBits = 12L;

    private long maxMachineRoomId = -1L ^ (-1L << machineRoomIdBits);

    private long maxComputerId = -1L ^ (-1L << computerIdBits);

    private long maxSequenceId = -1L ^ (-1L << sequenceIdBits);

    /**
     * 序号
     */
    private long sequence = 0L;
    /**
     * 机房号
     */
    private long machineRoomId;
    /**
     * 服务器号
     */
    private long computerId;

    private long lastTimeStamp;

    public SnowflakeWorkId(){}

    public SnowflakeWorkId(long machineRoomId,long computerId) {
        try {
            initCheck(machineRoomId,computerId);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param machineRoomId 输入机房编号
     * @param computerId 输入机器编号
     */
    private void initCheck(long machineRoomId, long computerId) throws IllegalAccessException {
        if(machineRoomId < 0 || machineRoomId > maxMachineRoomId){
            throw new IllegalAccessException(String.format("machineRoomId must be greater than zero and less than %d",maxMachineRoomId));
        }
        if(computerId < 0 || computerId > maxComputerId){
            throw new IllegalAccessException(String.format("computerId must be greater than zero and less than %d",maxComputerId));
        }
        this.machineRoomId = machineRoomId;
        this.computerId = computerId;
    }

    /**
     * 获取ID
     * @return
     */
    public long nextWorkId(){
        long timeStamp = nowTimeStamp();
        //回拨
        if(timeStamp < lastTimeStamp){
            throw new RuntimeException(String.format("The last timestamp is greater than the latest timestamp, with a value of %d", lastTimeStamp - timeStamp));
        }
        if(timeStamp == lastTimeStamp){
            sequence = (sequence + 1) & maxSequenceId;
            if(sequence == 0){
                timeStamp = getStartTime(lastTimeStamp);
            }
        }else {
            sequence = 0L;
        }
        lastTimeStamp = timeStamp;
        return (timeStamp - startTime) << (machineRoomIdBits+computerIdBits+sequenceIdBits) | (machineRoomId << computerIdBits+sequenceIdBits) | (computerId << sequenceIdBits) | sequence;
    }

    /**
     * 获取新的时间戳
     * @param lastTimeStamp
     * @return
     */
    protected long getStartTime(long lastTimeStamp){
        long timeStamp = nowTimeStamp();
        while (timeStamp == lastTimeStamp){
            timeStamp = nowTimeStamp();
        }
        lastTimeStamp = timeStamp;
        return timeStamp;
    }

    /**
     * 获取当前时间戳
     * @return
     */
    protected long nowTimeStamp(){
        return System.currentTimeMillis();
    }

}
