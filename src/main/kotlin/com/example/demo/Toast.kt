package com.example.demo

import javafx.animation.FadeTransition
import javafx.animation.TranslateTransition
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File




enum class ImageStyle {
    CIRCLE, RECTANGLE
}



class Config {
    var alpha = 0.9
    var openTime = 7000.0
    var imageType = ImageStyle.RECTANGLE
    var title = "TITLE"
    var message = "MESSAGE"
    var appName = "APP NAME"
    var image = "https://avatars.mds.yandex.net/i?id=ee0a8cd0c69a411b7fee131fde2b4980-3732926-images-thumbs&n=13"
    var screenPosition = "RIGHT BOTTOM"
}

class Toast {
    private var config = Config()
    private val windows = Stage()
    private var root = BorderPane()
    private var box = HBox()


    class Builder {
        private var config = Config()

        fun setTitle(str: String): Builder {
            config.title = str
            return this
        }

        fun setMessage(str: String): Builder {
            config.message = str;
            return this
        }

        fun setAppName(str: String): Builder {
            config.appName = str
            return this
        }

        fun build(): Toast  {
            var toast = Toast()
            toast.config = config
            toast.build()

            return toast
        }
    }


    private fun build() {






        //BUTTONS AND CHANGE PIC
        var btn = Button("NEXT PIC")
        btn.style = "-fx-background-color: Orange ; -fx-font-weight: Bold; -fx-font-style: italic;"
        btn.onAction = EventHandler {

            if (config.imageType == ImageStyle.CIRCLE) {
                config.imageType = ImageStyle.RECTANGLE
            } else config.imageType = ImageStyle.CIRCLE

            val iconBorder = if (config.imageType == ImageStyle.RECTANGLE) {
                Rectangle(100.0, 100.0)
            }
            else {
                Circle(50.0, 50.0, 50.0)
            }

            iconBorder.setFill(ImagePattern(Image(config.image)))
            box.children.set(box.children.lastIndex-1, iconBorder)
            windows.width += 100.0

        }
        root.children.add(btn)

        windows.initStyle(StageStyle.TRANSPARENT)

        if (config.screenPosition == "LEFT TOP"){
            windows.x = 0.0
            windows.y = 0.0
        }
        else if (config.screenPosition == "LEFT BOTTOM"){
            windows.x = 0.0
            windows.y = 440.0
        }
        else if (config.screenPosition == "RIGHT TOP"){
            windows.x = 1120.0
            windows.y = 0.0
        }
        else if (config.screenPosition == "RIGHT BOTTOM"){
            windows.x = 1220.0
            windows.y = 440.0
        }

        val width = 800.0
        val height = 650.0

        windows.scene = Scene(root, width, height)
        windows.scene.fill = Color.TRANSPARENT

        root.style = "-fx-background-color: #ffffff"
        root.setPrefSize(width, height)

        setImage()

        val vbox = VBox()

        val title = Label(config.title)
        val message = Label(config.message)
        val appName = Label(config.appName)

        //sssSTYLEE
        title.style="-fx-background-color: #FF7912; -fx-text-fill: white;\n" +
                "-fx-text-fill: black;\n" +
                "-fx-font-weight:bold;"
        message.style="-fx-background-color: linear-gradient(to right bottom,  #FF7812, #FBF2EB);\n" +
                "-fx-text-fill: black;\n" +
                "-fx-font-weight:bold;"
        appName.style="-fx-background-color: linear-gradient(to right bottom, #FF7812, #FBF2EB);\n" +
                "-fx-text-fill: black;\n" +
                "-fx-font-weight:bold;"



        vbox.children.addAll(title, message, appName,btn)
        box.children.add(vbox)
        root.center = box


    }

    private fun setImage() {
        if (config.image.isEmpty()) {
            return
        }

        val iconBorder = if (config.imageType == ImageStyle.RECTANGLE) {
            Rectangle(100.0, 100.0)
        }
        else {
            Circle(50.0, 50.0, 50.0)
        }
        iconBorder.setFill(ImagePattern(Image(config.image)))
        box.children.add(iconBorder)
    }

    //5
    private fun openAnimation() {

        val anim = FadeTransition(Duration.millis(1500.0), root)
        anim.fromValue = 0.0
        anim.toValue = config.alpha
        anim.cycleCount = 1
        anim.play()
    }

    private fun newOpenAnimation(){
        val anim = TranslateTransition(Duration.seconds(1.0), root)
        if (config.screenPosition == "LEFT TOP" || config.screenPosition == "LEFT BOTTOM"){
            anim.fromX = -800.0
            anim.toX = 0.0
        } else if (config.screenPosition == "RIGHT TOP" || config.screenPosition == "RIGHT BOTTOM") {
            anim.fromX = 1920.0
            anim.toX = 0.0
        }



        anim.cycleCount = 1
        anim.isAutoReverse = true
        anim.play()
    }

    private fun newCloseAnimation(){
        val anim = TranslateTransition(Duration.seconds(1.0), root)
        if (config.screenPosition == "LEFT TOP" || config.screenPosition == "LEFT BOTTOM"){
            anim.toX = -800.0
            anim.fromX = 0.0
        } else if (config.screenPosition == "RIGHT TOP" || config.screenPosition == "RIGHT BOTTOM") {
            anim.toX = 1920.0
            anim.fromX = 0.0
        }

        anim.cycleCount = 1
        anim.onFinished = EventHandler {
            Platform.exit()
            System.exit(0)
        }
        anim.play()
    }

    private fun closeAnimation() {
        val anim = FadeTransition(Duration.millis(1500.0), root)
        anim.fromValue = config.alpha
        anim.toValue = 0.0
        anim.cycleCount = 1
        anim.onFinished = EventHandler {
            Platform.exit()
            System.exit(0)
        }
        anim.play()
    }


    fun start() {
        windows.show()
//        openAnimation();



        newOpenAnimation()
        val thread = Thread {
            try {
                Thread.sleep(config.openTime.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            newCloseAnimation()
        }
        Thread(thread).start()
    }

}

    fun music(){
    val musicFile = "C:\\1.mp3"
    val sound = Media(File(musicFile).toURI().toString())
    val mediaPlayer = MediaPlayer(sound)}

class SomeClass: Application() {

    override fun start(p0: Stage?) {
        music()
        var toast = Toast.Builder()
            .setTitle("T.N.T")
            .setMessage("Iron Man 2")
            .setAppName("AC/DC")
            .build()
        toast.start()
    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(SomeClass::class.java)
        }
    }
}