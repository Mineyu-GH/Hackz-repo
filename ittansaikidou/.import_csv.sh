#!/bin/bash

# Redisのホストとポート
REDIS_HOST="127.0.0.1"
REDIS_PORT="6379"

# CSVファイルのパス
CSV_FILE="public/db/data.csv"

# キーの接頭辞 (任意)
KEY_PREFIX="key"

# カウンターを初期化
COUNTER=1

# CSVファイルを1行ずつ読み込む
while IFS= read -r line; do
    # Redisにデータを追加
    docker exec redis redis-cli -h $REDIS_HOST -p $REDIS_PORT SET "${KEY_PREFIX}:${COUNTER}" "$line"

#    redis-cli -h $REDIS_HOST -p $REDIS_PORT SET "${KEY_PREFIX}:${COUNTER}" "$line"

    # カウンターをインクリメント
    COUNTER=$((COUNTER + 1))
done < "$CSV_FILE"

echo "データをRedisにインポートしました。"
