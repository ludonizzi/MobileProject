package com.example.mobileproject

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import java.util.*
import kotlin.math.min


class Game : ApplicationAdapter() {
    var batch: SpriteBatch? = null
    var backGround: Texture? = null
    var birdsState = mutableListOf<Texture>()
    var topPipe: Texture? = null
    var bottomPipe: Texture? = null
    var gameOver: Texture? = null

    var size: Float = 0f

    var flapState = 0

    var birdY: Float = 0f
    var velocity: Float = 0f

    var gameState = 0
    var gravity = 1f

    var gap = 0f

    // Maximum up and down of the pipes
    var maxPipeOffset = 0f

    var randomGenerator: Random? = null

    val pipeVelocity = 4
    val numberOfPipes = 4
    var distHorBwPipes = 0
    var pipeX = FloatArray(numberOfPipes)

    var distVertBwPipes = FloatArray(numberOfPipes)

    //Collision detection
    var birdCircle: Circle? = null
    var topPipeRect = mutableListOf<Rectangle>()
    var bottomPipeRect = mutableListOf<Rectangle>()
//    var shapeRenderer: ShapeRenderer? = null

    // Scoring
    var score = 0
    var scoringPipe = 0
    var bitmapFont: BitmapFont? = null
    var scoreTextures = mutableListOf<Texture>()

    override fun create()
    {
        batch = SpriteBatch()

        size = min(
                (Gdx.graphics.width*0.1).toFloat(),
                (Gdx.graphics.height*0.1).toFloat()
        )

        birdCircle = Circle()
//        shapeRenderer = ShapeRenderer()

        bitmapFont = BitmapFont()
        bitmapFont!!.setColor(Color.WHITE)
        bitmapFont!!.data.scale(size/10)

        gap = size*4

        backGround = Texture("background-day.png")
        birdsState.add(Texture("blue_down.png"))
        birdsState.add(Texture("blue_mid.png"))
        birdsState.add(Texture("blue_up.png"))
        birdsState.add(Texture("blue_mid.png"))

        topPipe = Texture("topPipe.png")
        bottomPipe = Texture("bottomPipe.png")

        for(i in 0..9)
        {
            scoreTextures.add(Texture("${i}.png"))
        }

        gameOver = Texture("gameover.png")

        maxPipeOffset = (Gdx.graphics.height - gap - gap/2)

        randomGenerator = Random()

        distHorBwPipes = Gdx.graphics.width * 3/4

        val rotation = Gdx.input.rotation
        if (Gdx.input.nativeOrientation == Input.Orientation.Portrait && (rotation == 90 || rotation == 270) ||  //First case, the normal phone
            Gdx.input.nativeOrientation == Input.Orientation.Landscape && (rotation == 0 || rotation == 180)
        ) //Second case, the landscape device
            distHorBwPipes = Gdx.graphics.width * 2/4 else
            distHorBwPipes = Gdx.graphics.width * 3/4


        for (i in 0 until numberOfPipes)
        {
            topPipeRect.add(Rectangle())
            bottomPipeRect.add(Rectangle())
        }

        startGame()
    }

    private fun startGame()
    {
        velocity = 0f
        score = 0
        scoringPipe = 0

        birdY = (Gdx.graphics.height/2 - size/2)
        for (i in 0 until numberOfPipes)
        {
            pipeX[i] = (Gdx.graphics.width + i* distHorBwPipes).toFloat()
            distVertBwPipes[i] = (randomGenerator!!.nextFloat() - 0.5f) * maxPipeOffset
        }
    }

    override fun render()
    {
        Gdx.app.log("birdY", "$birdY")
        batch!!.begin()
//        shapeRenderer!!.begin(ShapeRenderer.ShapeType.Filled)
//        shapeRenderer!!.setColor(Color.BLUE)

        // Background
        batch!!.draw(backGround,
                0f, 0f,
                Gdx.graphics.width.toFloat(),
                Gdx.graphics.height.toFloat())

        if (gameState == 1)
        {
            if(pipeX[scoringPipe] < Gdx.graphics.width / 2)
            {
                score++

                Gdx.app.log("Score", "${score}")

                if(scoringPipe < numberOfPipes - 1)
                {
                    scoringPipe++
                }
                else
                {
                    scoringPipe = 0
                }
            }

            if(Gdx.input.justTouched())
            {
                velocity = (-gap*0.055).toFloat()
            }

            for (i in 0 until numberOfPipes)
            {
                if(pipeX[i] < -size*2)
                {
                    pipeX[i] = pipeX[i] + numberOfPipes * this.distHorBwPipes
                    distVertBwPipes[i] = (randomGenerator!!.nextFloat() - 0.5f) * maxPipeOffset
                }
                else
                {
                    pipeX[i] = pipeX[i] - pipeVelocity
                }

                // Top Pipe
                batch!!.draw(
                        topPipe,
                        pipeX[i],
                        (Gdx.graphics.height / 2 + gap / 2) + distVertBwPipes[i],
                        size*2,
                        (Gdx.graphics.height / 2 - gap / 2) + maxPipeOffset*0.5f
                )
                // Bottom Pipe
                batch!!.draw(
                        bottomPipe,
                        pipeX[i],
                        0f - (Gdx.graphics.height - gap - gap/2)*0.5f + distVertBwPipes[i],
                        size*2,
                        (Gdx.graphics.height/2 - gap/2) + maxPipeOffset*0.5f
                )
            }

            if(birdY > 0)
            {
                velocity += gravity
                birdY -= velocity
            }
            else
            {
                gameState = 2
            }

            when(flapState)
            {
                0 -> flapState = 1
                1 -> flapState = 2
                2 -> flapState = 3
                3 -> flapState = 0
            }
        }
        else if(gameState == 0)
        {
            if(Gdx.input.justTouched())
            {
                gameState = 1
            }
        }
        else if (gameState == 2)
        {
            batch!!.draw(
                    gameOver,
                    (Gdx.graphics.width / 2 - size*3),
                    Gdx.graphics.height /2 - size/2,
                    size*6,
                    size)

            //Save score on db
            DatabaseManager().getAndSetBestScore(score)

            if(Gdx.input.justTouched())
            {
                startGame()
                gameState = 1
            }
        }

        // Bird
        batch!!.draw(
                birdsState[flapState],
                (Gdx.graphics.width/2 - size/2),
                birdY,
                size,
                size)

//        bitmapFont!!.draw(batch, "$score", size, Gdx.graphics.height - size)

        val scoreDigits = mutableListOf<Int>()
        var _score = score
        if (_score == 0)
        {
            batch!!.draw(
                    scoreTextures[0],
                    size,
                    Gdx.graphics.height - size*2,
                    size,
                    size)
        }
        else
        {
            while (_score > 0)
            {
                scoreDigits.add(_score % 10)
                _score /= 10
            }
            scoreDigits.reverse()

            for (i in 0 until scoreDigits.count())
            {
                batch!!.draw(
                        scoreTextures[scoreDigits[i]],
                        size*(i+1),
                        Gdx.graphics.height - size*2,
                        size,
                        size)
            }
        }

        birdCircle!!.set(
                (Gdx.graphics.width/2f),
                birdY + size/2,
                size/2)

//        shapeRenderer!!.circle(
//                birdCircle!!.x,
//                birdCircle!!.y,
//                birdCircle!!.radius)

        // Collision Detection
        for (i in 0 until numberOfPipes)
        {
            topPipeRect[i].set(
                    pipeX[i],
                    (Gdx.graphics.height / 2 + gap / 2) + distVertBwPipes[i],
                    size*2,
                    (Gdx.graphics.height / 2 - gap / 2) + maxPipeOffset*0.5f)
//            shapeRenderer!!.rect(
//                    topPipeRect[i].x,
//                    topPipeRect[i].y,
//                    topPipeRect[i].width,
//                    topPipeRect[i].height
//            )

            bottomPipeRect[i].set(
                    pipeX[i],
                    0f - (Gdx.graphics.height - gap - gap/2)*0.5f + distVertBwPipes[i],
                    size*2,
                    (Gdx.graphics.height/2 - gap/2) + maxPipeOffset*0.5f)
//            shapeRenderer!!.rect(
//                    bottomPipeRect[i].x,
//                    bottomPipeRect[i].y,
//                    bottomPipeRect[i].width,
//                    bottomPipeRect[i].height
//            )

            if(Intersector.overlaps(birdCircle, topPipeRect[i]) ||
                    Intersector.overlaps(birdCircle, bottomPipeRect[i]))
            {
                gameState = 2
            }
        }

        batch!!.end()
//        shapeRenderer!!.end()
    }

    //override fun dispose() {
    //    batch!!.dispose()
    //}
}