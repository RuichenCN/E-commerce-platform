-- KEYS[1] PROMOTION_promotionId_STOCK
-- 1. check promotion stock exists or not
if redis.call("exists", KEYS[1]) == 1
    then
    -- 2. get key value
    local stock = tonumber(redis.call("get", KEYS[1]));
    if (stock > 0)
        then
        -- 3. stock > 0 then lock stock
        redis.call("set", KEYS[1], stock - 1);
        return stock - 1;
    end
    -- sold out
    return -1;
end
-- 4. not exist
return -2;