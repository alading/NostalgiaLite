package com.appblend.handfree.yaw




class Constants {
    companion object {


        val Movie_Title_Ella: String = "Ella"
        val Movie_Title_Tube_Slide: String = "Tube Slide"
        val Movie_Title_Water_Ski: String = "Water Ski"
        val Movie_Title_Mission_Impossible: String = "Mission Impossible"
        val Delay_During_Speaking: Long = 1000
        val Delay_Start_Speaking: Long = 1000
        const val EXTRA_CHAPTER_INFO = "Chapter_info"
        const val EXTRA_PROJECT_ID = "Project_id"
        const val EXTRA_NODE_INFO = "node_info"
        const val EXTRA_MOVIE_INFO = "movie_info"
        const val URL_SIGNED_COOKIE_SAMPLE = "https://d3jsxf53wr8zh6.cloudfront.net/740910923169529856_6_mrb/playlist.m3u8"
        const val URL_HLS_TEST = "https://s3.us-west-2.amazonaws.com/minestory-resource-data-dev/resource/bundle/786611290533388288/file/768443694316773376_551/360p.m3u8"
        //const val URL_VIDEO_NOT_FOUND = "https://ideo-project.s3.amazonaws.com/video/videonotfound.mp4"
        const val URL_VIDEO_NOT_FOUND ="android.resource://com.tcl.tv.ideo.player/raw/videonotfound"
        const val URL_VIDEO_MISSION_IMPOSSIBLE ="android.resource://com.tcl.tv.ideo.player/raw/mission_impossible"
        const val URL_VIDEO_WATER_SKI ="android.resource://com.tcl.tv.ideo.player/raw/waterski"
        const val URL_VIDEO_TUBE_SLIDE ="asset:///tubeslide.mp4"
        const val URL_VIDEO_ELLA ="asset:///ella.mp4"
        const val DEBUG_NETZYN_DEEPLINK = false
        const val FastForward_Steps_MS: Long= 5000
        const val Rewind_Steps_MS: Long= 5000


        lateinit var MOVIE_VERSION: String

        const val SD_QUALITY_PATH = "/360p.m3u8"
        const val MD_QUALITY_PATH = "/480p.m3u8"
        const val HD_QUALITY_PATH = "/720p.m3u8"
        const val XD_QUALITY_PATH = "/1080p.m3u8"
        const val ALL_QUALITY_PATH = "/playlist.m3u8"

        lateinit var refreshToken: String
        lateinit var accessToken: String
        lateinit var token: String
        val header = "SWRlbzNkOklkZW8zZFNlY3JldAo="
        val username = "default"
        val password = "default"
        val bearer = "bearer"

        var showIntro = false;

        const val URL_AliCloud = "https://g.alicdn.com/ygfe/cg-sdk-test/game.html?YXBwS2V5PTMzMzM2NTM0NSZ1c2VySWQ9dGVzdHVzZXIxJnRva2VuPXRlc3R1c2VyMSZtaXhHYW1lSWQ9Y2diYWllYWZsbnEmZW52PXByb2QmZXhwaXJlVGltZT0xNjIwODk1MDUyNjI0"
        const val URL_Momento360 = "https://momento360.com/e/u/6b2cd4e6160b42db8d6f939b5afd4685"
        const val URL_Google = "https://image.google.com"

        //Tutorial Pref Key
        const val KEY_SHOW_TUTORIAL = "key_tutorial"

        //demo 7D
        var demo7D = true


        // Yaw Chair
        var Yaw_Chair_IpAddress : String? = null
        val TCP_PORT = 50020
        val UDP_PORT = 50010
        val START = 0xA1
        val STOP = 0xA2
        val EXIT = 0xA3
        val YAW_Offset = 500;
    }
}