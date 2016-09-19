package org.bigbluebutton.endpoint.redis

import akka.actor.ActorSystem
import org.bigbluebutton.red5apps.SystemConfiguration
import redis.RedisClient

class RedisPublisher(val system: ActorSystem) extends SystemConfiguration {

  val redis = RedisClient(redisHost, redisPort)(system)

  // Set the name of this client to be able to distinguish when doing
  // CLIENT LIST on redis-cli
  redis.clientSetname("Red5AppsAkkaPub")

  def publish(channel: String, data: String) {
    //println("PUBLISH TO [" + channel + "]: \n [" + data + "]")
    redis.publish(channel, data)
  }

}