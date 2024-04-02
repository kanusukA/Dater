@file:OptIn(ExperimentalFoundationApi::class)

package com.example.dater.ui.daterTextBox


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dater.R
import com.example.dater.ui.components.TopFilterBar.shapes.TopBarSearchTextFieldInputs
import com.example.dater.ui.components.TopFilterBar.shapes.TopBarSearchTextFieldShape

@ExperimentalFoundationApi
@Composable
fun DaterTextField(
    modifier : Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    textColor: Color,
    textSize: Int = 16,
    fontFamily: FontFamily,
    label: String,
    backgroundColor: Color = Color.White.copy(alpha = 0.3f),
    textBoxColor: Color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f),
    gapLength: Dp = 80.dp,
    gapHeight: Dp = 2.dp,
    paddingStart: Dp = 12.dp,
    cornerRadius: Dp = 20.dp,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default
    ) {

    val shape = TopBarSearchTextFieldShape(
        TopBarSearchTextFieldInputs(
            gapLength = gapLength,
            gapHeight = gapHeight,
            paddingStart = paddingStart,
            cornerRadius = cornerRadius
        )
    )

    Box(modifier = modifier
        .background(backgroundColor, shape = shape)
    ){
        Text(
            modifier = Modifier.padding(start = paddingStart + cornerRadius + 12.dp),
            text = label,
            fontFamily = FontFamily(Font(R.font.rubik_font)),
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = textColor
        )

        Box(modifier = Modifier
            .align(Alignment.Center)
            .padding(top = gapHeight + cornerRadius + 6.dp, start = 8.dp, end = 8.dp , bottom = 6.dp)
            .background(textBoxColor, shape = RoundedCornerShape(20.dp))
        ){

            BasicTextField2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = text,
                onValueChange = {onValueChange(it)},
                textStyle = TextStyle(
                    color = textColor,
                    fontSize = textSize.sp,
                    fontFamily = fontFamily
                ),
                lineLimits = lineLimits
            )
        }
    }

}

