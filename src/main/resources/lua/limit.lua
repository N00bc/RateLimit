--[[
    lua中数组的index从1开始不是0
    此脚本作用:返回当前接口请求次数
]]
-- 将KEYS数组的第一个值赋给key
local key = KEYS[1]
-- 将ARGV数组的第一个值转换成数字再赋值给time
local time = tonumber(ARGV[1])
-- 将ARGV数组的第二个值转换成数字再赋值给count
local count = tonumber(ARGV[2])
-- 调用redis的 get 方法,通过key获取当前的请求次数
local current = redis.call('get', key)
-- 如果current存在 且 当前的请求次数已经超过阈值count,那么说明当前key需要被限流
if current and tonumber(current) > count then
    return tonumber(current)
end
-- 当前key无记录，说明是第一次请求，需要给 current 自增1
current = redis.call('incr', key)
if tonumber(current) == 1 then
    -- 如果是第一次请求，需要给当前key设置过期时间
    redis.call('expire',key,time)
end
return tonumber(current)