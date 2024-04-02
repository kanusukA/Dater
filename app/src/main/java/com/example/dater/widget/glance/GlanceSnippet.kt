package com.example.dater.widget.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.dater.Data.Journey.dataSource.JourneyDatabase
import com.example.dater.Data.Journey.dataSource.repository.JourneyRepositoryImpli
import com.example.dater.Data.utils.DateHandler
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.unit.ColorProvider
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Journey.domain.model.JourneyWidgetSelection
import com.example.dater.MainActivity


class DaterWidget : GlanceAppWidget() {


    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {


        provideContent {
            GlanceTheme {

                MyContent(context = context)

            }

        }

    }

    @Composable
    fun MyContent(context: Context) {
        val journeyDatabase = JourneyDatabase.getDataBase(context)
        val journeyRepository = JourneyRepositoryImpli(journeyDatabase.journeyDao(),context)

        val journeySelection: JourneyWidgetSelection =
            journeyRepository.getJourneySelection().collectAsState(
                JourneyWidgetSelection()
            ).value

        var primaryJourney by remember {
            mutableStateOf(Journey(0L, 0L, title = ""))
        }

        var secondaryJourney by remember {
            mutableStateOf(Journey(0L, 0L, title = ""))
        }
        var percentage by remember {
            mutableFloatStateOf(0f)
        }

        LaunchedEffect(key1 = journeySelection,key2 = true) {
            primaryJourney =
                journeyRepository.getJourneyById(journeySelection.primaryJourneyId) ?: Journey(
                    0L,
                    0L,
                    title = ""
                )
            secondaryJourney =
                journeyRepository.getJourneyById(journeySelection.secondaryJourneyId) ?: Journey(
                    0L,
                    0L,
                    title = ""
                )
            percentage = DateHandler().getCompletionPercentage(primaryJourney.startDate,primaryJourney.endDate)
        }

        ProgressWidgetBar(
            percentage = 0.331f,
            paddingFromEdges = 6.dp,
            strokeSize = 32.dp,
            backgroundColor = GlanceTheme.colors.background,
            lineBackGroundColor = GlanceTheme.colors.primaryContainer,
            lineForeGroundColor = GlanceTheme.colors.primary,
            ballColor = GlanceTheme.colors.tertiary
        ){
            SimpleWidgetBox(
                primaryJourney = primaryJourney,
                secondaryJourney = secondaryJourney
            )
        }

    }


    @Composable
    private fun ProgressWidgetBar(
        percentage: Float,
        paddingFromEdges: Dp,
        strokeSize: Dp,
        backgroundColor: ColorProvider,
        lineBackGroundColor: ColorProvider,
        lineForeGroundColor: ColorProvider,
        ballColor: ColorProvider,
        content: @Composable  () -> Unit
    ){

        val size = LocalSize.current

        val width = size.width - paddingFromEdges
        val height = size.height - paddingFromEdges

        val ballSize = strokeSize - (paddingFromEdges * 2)

        val viewStrokeSize = strokeSize - (paddingFromEdges * 2)


        val percentageLength by remember(key1 = percentage,key2 = width,key3 = height) {
            mutableStateOf(
                ((width * 2) + (height * 2)) * percentage
            )
        }

        var curvePadding by remember {
            mutableStateOf(0.dp)
        }

        val ballAlignment by remember(key1 = percentageLength) {
            mutableStateOf(
                if(percentageLength >= (width * 2) + height){
                    Alignment.TopStart
                } else if (percentageLength >= width  + height){
                    Alignment.BottomStart
                } else if (percentageLength >= width){
                    Alignment.TopEnd
                } else {
                    Alignment.TopStart
                }
            )
        }

        var ballTopPadding by remember {
            mutableStateOf(0.dp)
        }

        var ballStartPadding by remember {
            mutableStateOf(0.dp)
        }

        var ballEndPadding by remember {
            mutableStateOf(0.dp)
        }

        var ballBottomPadding by remember {
            mutableStateOf(0.dp)
        }


        var topPercentage by remember(key1 = percentageLength) {
            mutableFloatStateOf(
                if(percentageLength >= width){
                    1f
                } else{
                    (percentageLength / width)
                }
            )
        }

        var endPercentage by remember(key1 = percentageLength) {
            mutableFloatStateOf(
                if(percentageLength >= (width + height)){
                    1f
                } else if(percentageLength > width) {
                    percentageLength / (width + height)
                } else {
                    0f
                }
            )
        }

        var bottomPercentage by remember(key1 = percentageLength) {
            mutableFloatStateOf(
                if(percentageLength >= ((width * 2) + height)){
                    1f
                } else if (percentageLength > (width + height)) {
                    percentageLength / ((width * 2) + height)
                } else {
                    0f
                }
            )
        }

        var startPercentage by remember(key1 = percentageLength) {
            mutableFloatStateOf(
                if(percentageLength >= ((width * 2) + (height * 2))){
                    1f
                } else if (percentageLength > ((width * 2) + height)){
                    percentageLength / ((width * 2) + (height * 2))
                } else {
                    0f
                }
            )
        }

        LaunchedEffect(key1 = percentageLength , key2 = true){
            if(percentageLength >= (width * 2) + height){

                Alignment.TopStart
                ballTopPadding = height - ((height * startPercentage) + (ballSize / 2))
                ballStartPadding = 0.dp

            } else if (percentageLength >= width  + height){

                if((width * bottomPercentage) >= width - 20.dp){
                    curvePadding = width - (width * bottomPercentage)
                    startPercentage = curvePadding / height
                } else {
                    curvePadding = 0.dp
                }

                Alignment.BottomStart
                ballStartPadding = width - ((width * bottomPercentage) + (ballSize / 2))
                ballTopPadding = 0.dp
                ballBottomPadding = (curvePadding / 2)
                ballEndPadding = 0.dp

            } else if (percentageLength >= width){

                if(height * endPercentage >= height - 20.dp){
                    curvePadding = height - (height * endPercentage)
                    bottomPercentage = curvePadding / width
                } else {
                    curvePadding = 0.dp
                }

                Alignment.TopEnd
                ballTopPadding = (height * endPercentage) - (ballSize / 2)
                ballStartPadding = 0.dp
                ballEndPadding = (curvePadding / 2)
                ballBottomPadding = 0.dp

            } else {

                if (width * topPercentage >= width - 20.dp){
                    curvePadding = width - (width * topPercentage)
                    endPercentage = curvePadding / height
                } else {
                    curvePadding = 0.dp
                }

                Alignment.TopStart
                ballStartPadding = ((width * topPercentage) - (ballSize / 2)) + (curvePadding / 2)
                ballTopPadding = (curvePadding / 2)
                ballBottomPadding = 0.dp
                ballEndPadding = 0.dp
            }
        }


        // SURFACE
        Box (
            modifier = GlanceModifier
                .fillMaxSize()
                .background(backgroundColor)
                ,
            contentAlignment = Alignment.Center
        ){

            // BACKGROUND LINE
            Box (
                modifier = GlanceModifier
                    .size(width, height)
                    .background(lineBackGroundColor)
                    .cornerRadius(20.dp),
                content = {

                    // TOP LINE
                    Box (
                        modifier = GlanceModifier
                            .size(width * topPercentage,viewStrokeSize)
                            .background(lineForeGroundColor),
                    ){}

                }
            )

            // END LINE
            Box(
                modifier = GlanceModifier
                    .size(width, height)
                    .cornerRadius(20.dp)
                ,
                contentAlignment = Alignment.TopEnd
            ){
                Box (
                    GlanceModifier
                        .size(viewStrokeSize,height * endPercentage)
                        .background(lineForeGroundColor)
                ){}
            }

            // BOTTOM LINE
            Box(
                modifier = GlanceModifier
                    .size(width, height)
                    .cornerRadius(20.dp)
                ,
                contentAlignment = Alignment.BottomEnd
            ){
                Box (
                    GlanceModifier
                        .size(width * bottomPercentage,viewStrokeSize)
                        .background(lineForeGroundColor)
                ){}
            }

            // START LINE

            Box (
                modifier = GlanceModifier
                    .size(width, height)
                    .cornerRadius(20.dp)
                ,
                contentAlignment = Alignment.BottomStart
            ){
                Box (
                    modifier = GlanceModifier
                        .size(viewStrokeSize,height * startPercentage)
                        .background(lineForeGroundColor)
                ){}
            }


            // BACKGROUND CENTER FILL
            Box (
                modifier = GlanceModifier
                    .size(width - strokeSize, height - strokeSize)
                    .background(backgroundColor)
                    .cornerRadius(10.dp),
                content = content
            )



        }

        // LINE BALL
        Box (
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(start = (paddingFromEdges / 2) , top = (viewStrokeSize + (paddingFromEdges / 2)) / 2)
        ){
            Box (
                modifier = GlanceModifier
                    .size(viewStrokeSize - (paddingFromEdges / 4))
                    .background(lineForeGroundColor)
                    .cornerRadius(12.dp)
            ){

            }

        }

        // ADD BALLS
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(top = ballTopPadding, start = ballStartPadding, end = ballEndPadding, bottom = ballBottomPadding),
            contentAlignment = ballAlignment
        ){
            Box(
                modifier = GlanceModifier
                    .size(ballSize)
                    .background(ballColor)
                    .cornerRadius(12.dp)
            ) {}
        }
    }


    @Composable
    private fun SimpleWidgetBox(primaryJourney: Journey, secondaryJourney: Journey) {


        val title = primaryJourney.title.ifBlank { secondaryJourney.title }
        val timeLeft = if (primaryJourney.title.isBlank()) {
            DateHandler().getDaysLeft(secondaryJourney.endDate)
        } else {
            DateHandler().getDaysLeft(primaryJourney.endDate)
        }

        val size = LocalSize.current

        if (primaryJourney.title.isNotBlank() || secondaryJourney.title.isNotBlank()) {
            if (size.width < 60.dp && size.height < 100.dp) {

                // Single
                DaterWidgetSmallSize(timeLeft = timeLeft)

            } else if (size.width <= 210.dp && 135.dp < size.width && size.height < 100.dp) {

                // Single Full
                SingleCell(title = title, timeLeft = timeLeft)

            } else if (size.height >= 100.dp && size.width >= 120.dp) {

                //Double
                if(secondaryJourney.title.isNotBlank() && primaryJourney.title.isNotBlank()){
                    DoubleCell(firstJourney = primaryJourney, secJourney = secondaryJourney)
                }else{
                    SingleCellWithSpace(title = title, timeLeft = timeLeft)
                }

            } else {

                DaterWidgetSmallSize(timeLeft = timeLeft)

            }
        } else {
            EmptyWidget()
        }

    }

}

@Composable
private fun EmptyWidget(){
    Box (modifier = GlanceModifier
        .fillMaxSize()
        .background(GlanceTheme.colors.primaryContainer)
        ,
        contentAlignment = Alignment.Center
    ){

        Button(text = "Add", onClick = actionStartActivity<MainActivity>())

    }
}

@Composable
private fun DaterWidgetSmallSize(timeLeft: Int) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$timeLeft",
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )

    }
}
// Add Buttons
@Composable
private fun SingleCellWithSpace(title: String, timeLeft: Int){
    Column (
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.primaryContainer)
    ){
        Spacer(modifier = GlanceModifier.height(24.dp))

        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .cornerRadius(20.dp)
                .background(GlanceTheme.colors.primaryContainer)
        ) {
            Text(
                modifier = GlanceModifier.padding(start = 6.dp, top = 6.dp, end = 6.dp),
                text = title,
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1
            )

            Text(
                modifier = GlanceModifier.padding(start = 48.dp, bottom = 6.dp,end = 6.dp),
                text = timeLeft.toString(),
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Box(modifier = GlanceModifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Button(
                text = "Add More",
                onClick = actionStartActivity<MainActivity>(),
                style = TextStyle(
                    color = GlanceTheme.colors.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
        }

    }
}

@Composable
private fun SingleCell(title: String, timeLeft: Int) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1
        )

        Text(
            text = timeLeft.toString(),
            style = TextStyle(
                color = GlanceTheme.colors.onPrimaryContainer,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun DoubleCell(firstJourney: Journey, secJourney: Journey) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .cornerRadius(20.dp)
                .background(GlanceTheme.colors.primaryContainer)
        ) {
            Text(
                modifier = GlanceModifier.padding(start = 6.dp, top = 6.dp, end = 6.dp),
                text = firstJourney.title,
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1
            )

            Text(
                modifier = GlanceModifier.padding(start = 48.dp, bottom = 6.dp,end = 6.dp),
                text = DateHandler().getDaysLeft(firstJourney.endDate).toString(),
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = GlanceModifier.height(24.dp))

        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .cornerRadius(20.dp)
                .background(GlanceTheme.colors.primaryContainer)
        ) {
            Text(
                modifier = GlanceModifier.padding(start = 6.dp, top = 6.dp, end = 6.dp),
                text = secJourney.title,
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1
            )

            Text(
                modifier = GlanceModifier.padding(start = 48.dp, bottom = 6.dp,end = 6.dp),
                text = DateHandler().getDaysLeft(secJourney.endDate).toString(),
                style = TextStyle(
                    color = GlanceTheme.colors.onPrimaryContainer,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

    }
}


class DaterAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DaterWidget()

}
