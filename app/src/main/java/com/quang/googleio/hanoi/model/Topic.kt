package com.quang.googleio.hanoi.model

class Topic {
    var id: String? = null
    var level: String? = null
    var duration: String? = null
    var name: String? = null
    var content: String? = null
    var location: String? = null
    var color: String? = null
    var topictype: String? = null
    var timestart: String? = null
    var timeend: String? = null
    var start: String? = null
    var speaker: String? = null
    var isBooked: Boolean = false

    constructor() {}

    constructor(id: String, level: String, duration: String, name: String, content: String,
                location: String, color: String, topictype: String, timestart: String,
                timeend: String, start: String, speaker: String, isBooked: Boolean) {
        this.id = id
        this.level = level
        this.duration = duration
        this.name = name
        this.content = content
        this.location = location
        this.color = color
        this.topictype = topictype
        this.timestart = timestart
        this.timeend = timeend
        this.start = start
        this.speaker = speaker
        this.isBooked = isBooked
    }

    constructor(id: String) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        return this.id == (other as Topic).id
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
