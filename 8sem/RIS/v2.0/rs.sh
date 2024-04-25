#!/bin/bash

# Ожидаем, пока MongoDB станет доступной
until mongosh mongodb://mongodb; do
    sleep 5
done

# Инициируем набор реплик
mongosh mongodb://mongodb <<EOF
  rs.initiate(
    {
      _id : "rs0",
      members: [
        { _id: 0, host: "mongodb:27017" },
        { _id: 1, host: "mongodb-1:27017" },
        { _id: 2, host: "mongodb-2:27017" }
      ]
    }
  )
  cfg = rs.conf()
  cfg.settings = {
      writeConcern: { w: "majority" },
      readConcern: { level: "majority" }
  }
  rs.reconfig(cfg)
EOF