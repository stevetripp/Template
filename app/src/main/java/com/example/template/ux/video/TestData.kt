package com.example.template.ux.video

import com.example.template.domain.VideoId
import com.example.template.domain.VideoRenditionType
import com.example.template.domain.VideoUrl
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class TestData(
    val videos: List<VideoItem>
) {
    companion object {
        @Suppress("LongMethod")
        fun getVideos(): TestData {
            // Generated using https://gospel-stream-service.churchofjesuschrist.org/stream/collections?id=03bf34b5be1cc2801197e4c221bfce12df27809b
            val json = """
{
  "videos": [
    {
      "hlsUrl": "https://mediasrv.churchofjesuschrist.org/media-services/GA/type/1288182088001/hls.m3u8",
      "seoPath": "/video/2010-11-0110-mormon-mustang-eng",
      "id": "17e0094fd9635e9e9737b7e105c7679b265736f6",
      "title": "Mormon Mustang",
      "duration": 463000,
      "description": "While flying in a historic airplane, youth learn about the dangers of pushing the envelope.",
      "imageRenditions": "60x34,https://www.churchofjesuschrist.org/imgs/275f027e4d9a622c5a692b393bf42f41e03c7146/full/60%2C/0/default\n100x56,https://www.churchofjesuschrist.org/imgs/275f027e4d9a622c5a692b393bf42f41e03c7146/full/100%2C/0/default\n200x112,https://www.churchofjesuschrist.org/imgs/275f027e4d9a622c5a692b393bf42f41e03c7146/full/200%2C/0/default\n250x141,https://www.churchofjesuschrist.org/imgs/275f027e4d9a622c5a692b393bf42f41e03c7146/full/250%2C/0/default\n320x180,https://www.churchofjesuschrist.org/imgs/275f027e4d9a622c5a692b393bf42f41e03c7146/full/320%2C/0/default\n500x281,https://www.churchofjesuschrist.org/imgs/275f027e4d9a622c5a692b393bf42f41e03c7146/full/500%2C/0/default\n640x360,https://www.churchofjesuschrist.org/imgs/275f027e4d9a622c5a692b393bf42f41e03c7146/full/640%2C/0/default\n800x450,https://www.churchofjesuschrist.org/imgs/275f027e4d9a622c5a692b393bf42f41e03c7146/full/800%2C/0/default",
      "imageAssetId": "275f027e4d9a622c5a692b393bf42f41e03c7146",
      "videoRenditions": [
        {
          "size": [
            1920,
            1080
          ],
          "url": "https://media2.ldscdn.org/assets/youth/from-every-nation-2010/2010-11-0110-mormon-mustang-1080p-eng.mp4",
          "type": "VIDEO",
          "fileSize": 467514511
        },
        {
          "size": [
            1280,
            720
          ],
          "url": "https://media2.ldscdn.org/assets/youth/from-every-nation-2010/2010-11-0110-mormon-mustang-720p-eng.mp4",
          "type": "VIDEO",
          "fileSize": 64329170
        },
        {
          "size": [
            480,
            360
          ],
          "url": "https://media2.ldscdn.org/assets/youth/from-every-nation-2010/2010-11-0110-mormon-mustang-360p-eng.mp4",
          "type": "VIDEO",
          "fileSize": 53796208
        }
      ],
      "captionUrl": "https://media.ldscdn.org/webvtt/youth/from-every-nation-2010/2010-11-0110-mormon-mustang-eng.vtt"
    },
    {
      "hlsUrl": "https://mediasrv.churchofjesuschrist.org/media-services/GA/type/3744035469001/hls.m3u8",
      "seoPath": "/video/2014-06-1130-obedience-brings-blessings-eng",
      "id": "0b2e66c62ad8fe015ad8dabcbe7c918f06b7b29c",
      "title": "Obedience Brings Blessings",
      "duration": 123000,
      "description": "President Monson talks on blessings of obedience.",
      "imageRenditions": "60x34,https://www.churchofjesuschrist.org/imgs/fbe65e315faff7f520a37040aabb45e9f1a35525/full/60%2C/0/default\n100x56,https://www.churchofjesuschrist.org/imgs/fbe65e315faff7f520a37040aabb45e9f1a35525/full/100%2C/0/default\n200x113,https://www.churchofjesuschrist.org/imgs/fbe65e315faff7f520a37040aabb45e9f1a35525/full/200%2C/0/default\n250x141,https://www.churchofjesuschrist.org/imgs/fbe65e315faff7f520a37040aabb45e9f1a35525/full/250%2C/0/default\n320x180,https://www.churchofjesuschrist.org/imgs/fbe65e315faff7f520a37040aabb45e9f1a35525/full/320%2C/0/default\n500x281,https://www.churchofjesuschrist.org/imgs/fbe65e315faff7f520a37040aabb45e9f1a35525/full/500%2C/0/default\n640x360,https://www.churchofjesuschrist.org/imgs/fbe65e315faff7f520a37040aabb45e9f1a35525/full/640%2C/0/default\n800x450,https://www.churchofjesuschrist.org/imgs/fbe65e315faff7f520a37040aabb45e9f1a35525/full/800%2C/0/default\n1280x720,https://www.churchofjesuschrist.org/imgs/fbe65e315faff7f520a37040aabb45e9f1a35525/full/1280%2C/0/default\n1600x900,https://www.churchofjesuschrist.org/imgs/fbe65e315faff7f520a37040aabb45e9f1a35525/full/1600%2C/0/default\n1920x1080,https://www.churchofjesuschrist.org/imgs/fbe65e315faff7f520a37040aabb45e9f1a35525/full/1920%2C/0/default",
      "imageAssetId": "fbe65e315faff7f520a37040aabb45e9f1a35525",
      "videoRenditions": [
        {
          "size": [
            1920,
            1080
          ],
          "url": "https://media2.ldscdn.org/assets/welfare/pef-self-reliance-curriculum/2014-06-1130-obedience-brings-blessings-720p-eng.mp4",
          "type": "VIDEO",
          "fileSize": 91011
        },
        {
          "size": [
            1280,
            720
          ],
          "url": "https://media2.ldscdn.org/assets/welfare/pef-self-reliance-curriculum/2014-06-1130-obedience-brings-blessings-720p-eng.mp4",
          "type": "VIDEO",
          "fileSize": 28021896
        },
        {
          "size": [
            480,
            360
          ],
          "url": "https://media2.ldscdn.org/assets/welfare/pef-self-reliance-curriculum/2014-06-1130-obedience-brings-blessings-360p-eng.mp4",
          "type": "VIDEO",
          "fileSize": 20460316
        }
      ]
    },
    {
      "hlsUrl": "https://mediasrv.churchofjesuschrist.org/media-services/GA/type/2651914142001/hls.m3u8",
      "seoPath": "/video/2012-05-0209-teachings-of-brigham-young-hard-work-sacrifice-obedience-eng",
      "id": "aaa855b72c9db4bd9f5c264be699c62f86f9b2bb",
      "title": "Teachings of Brigham Young: Hard Work, Sacrifice, Obedience",
      "duration": 123000,
      "description": "Brigham Young describes the blessings that come from faithfully facing life's challenges.",
      "videoRenditions": [
        {
          "size": [
            1920,
            1080
          ],
          "url": "https://media2.ldscdn.org/assets/church-history/church-history-presidents-of-the-church/2012-05-0209-teachings-of-brigham-young-hard-work-sacrifice-obedience-720p-eng.mp4",
          "type": "VIDEO",
          "fileSize": 91011
        },
        {
          "size": [
            1280,
            720
          ],
          "url": "https://media2.ldscdn.org/assets/church-history/church-history-presidents-of-the-church/2012-05-0209-teachings-of-brigham-young-hard-work-sacrifice-obedience-720p-eng.mp4",
          "type": "VIDEO",
          "fileSize": 10751643
        },
        {
          "size": [
            480,
            360
          ],
          "url": "https://media2.ldscdn.org/assets/church-history/church-history-presidents-of-the-church/2012-05-0209-teachings-of-brigham-young-hard-work-sacrifice-obedience-360p-eng.mp4",
          "type": "VIDEO",
          "fileSize": 14087764
        }
      ]
    },
    {
      "hlsUrl": "https://mediasrv.churchofjesuschrist.org/media-services/GA/type/5204028326001/hls.m3u8",
      "seoPath": "/video/2016-01-0011-choose-the-light-eng",
      "id": "b0d3b7c2c9e92dfc14476e9cef43d9af99541f56",
      "title": "Choose the Light",
      "duration": 422000,
      "description": "A reckless biker finds himself in a battle against time and the elements when he enters a situation he can’t get out of. He discovers his only option is to choose the light.",
      "imageRenditions": "60x34,https://www.churchofjesuschrist.org/imgs/eee9f120245ce8137f2cf2562ad19bcb43b6e2ad/full/60%2C/0/default\n100x56,https://www.churchofjesuschrist.org/imgs/eee9f120245ce8137f2cf2562ad19bcb43b6e2ad/full/100%2C/0/default\n200x113,https://www.churchofjesuschrist.org/imgs/eee9f120245ce8137f2cf2562ad19bcb43b6e2ad/full/200%2C/0/default\n250x141,https://www.churchofjesuschrist.org/imgs/eee9f120245ce8137f2cf2562ad19bcb43b6e2ad/full/250%2C/0/default\n320x180,https://www.churchofjesuschrist.org/imgs/eee9f120245ce8137f2cf2562ad19bcb43b6e2ad/full/320%2C/0/default\n500x281,https://www.churchofjesuschrist.org/imgs/eee9f120245ce8137f2cf2562ad19bcb43b6e2ad/full/500%2C/0/default\n640x360,https://www.churchofjesuschrist.org/imgs/eee9f120245ce8137f2cf2562ad19bcb43b6e2ad/full/640%2C/0/default\n800x450,https://www.churchofjesuschrist.org/imgs/eee9f120245ce8137f2cf2562ad19bcb43b6e2ad/full/800%2C/0/default\n1280x720,https://www.churchofjesuschrist.org/imgs/eee9f120245ce8137f2cf2562ad19bcb43b6e2ad/full/1280%2C/0/default\n1600x900,https://www.churchofjesuschrist.org/imgs/eee9f120245ce8137f2cf2562ad19bcb43b6e2ad/full/1600%2C/0/default\n1920x1080,https://www.churchofjesuschrist.org/imgs/eee9f120245ce8137f2cf2562ad19bcb43b6e2ad/full/1920%2C/0/default",
      "imageAssetId": "eee9f120245ce8137f2cf2562ad19bcb43b6e2ad",
      "videoRenditions": [
        {
          "size": [
            1920,
            1080
          ],
          "url": "https://media2.ldscdn.org/assets/youth/2016-youth-media/2016-01-011-choose-the-light-1080p-eng.mp4",
          "type": "VIDEO",
          "fileSize": 351158724
        },
        {
          "size": [
            1280,
            720
          ],
          "url": "https://media2.ldscdn.org/assets/youth/2016-youth-media/2016-01-011-choose-the-light-720p-eng.mp4",
          "type": "VIDEO",
          "fileSize": 92526676
        },
        {
          "size": [
            480,
            360
          ],
          "url": "https://media2.ldscdn.org/assets/youth/2016-youth-media/2016-01-011-choose-the-light-360p-eng.mp4",
          "type": "VIDEO",
          "fileSize": 43802409
        }
      ],
      "captionUrl": "https://media.ldscdn.org/webvtt/youth/2016-youth-media/2016-01-011-choose-the-light-eng.vtt"
    }
  ]
}"""
            return Json.decodeFromString(json)
        }
    }
}

@Serializable
data class VideoRendition(
    val url: VideoUrl,
    val size: List<Int>,
    val fileSize: Int,
    val type: VideoRenditionType
)

@Serializable
data class VideoItem(
    val duration: Int,
    val imageRenditions: String = "",
    val imageAssetId: String = "",
    val description: String,
    val seoPath: String,
    val videoRenditions: List<VideoRendition>,
    val id: VideoId,
    val title: String,
    val captionUrl: String = "",
    val hlsUrl: VideoUrl,
)

