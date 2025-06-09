package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.solobookkeeping.ui.theme.SoloBookkeepingTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.solobookkeeping.R

@Composable
fun AccountScreen(modifier: Modifier = Modifier) {
    Column(

        modifier = modifier
            .fillMaxWidth()
            .padding(30.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {

        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(GifDecoder.Factory())
            }
            .build()
        Box(
            modifier = Modifier
                .size(175.dp),
            contentAlignment = Alignment.Center
        ){
            Box(
                modifier = Modifier
                    .size(175.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.skadi)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "Local GIF",
                modifier = Modifier.size(150.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "UserName : Ocean Cat",
            fontSize = 24.sp )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth()
                .height(60.dp),
            shape = MaterialTheme.shapes.small,
            onClick = { },
        ) {
            Text(
                text = "匯入資料",
                fontSize = 24.sp
                )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth()
                .height(60.dp),
            shape = MaterialTheme.shapes.small,
            onClick = { },
        ) {
            Text(
                text = "匯出資料",
                fontSize = 24.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth()
                .height(60.dp),
            shape = MaterialTheme.shapes.small,
            onClick = { },
        ) {
            Text(
                text = "財金教學",
                fontSize = 24.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.fillMaxWidth()
                .height(60.dp),
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
              containerColor = Color.Red
            ),
            onClick = { },
        ) {
            Text(
                text = "Logout Account",
                fontSize = 24.sp
            )
        }



    }
}

@Preview
@Composable
private fun AccountScreenPreview() {
    SoloBookkeepingTheme {
        AccountScreen()
    }
}