package com.example.mindwalk.ui.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindwalk.R // R 파일을 임포트해야 합니다.

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // 전체 박스를 중앙 정렬
    ) {
        // 1. 그라데이션 배경 이미지 (가장 아래 레이어)
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // 화면에 꽉 차게 자르거나, ContentScale.FillBounds 등으로 조절
        )

        // 2. 발자국 패턴 이미지 (배경 위에 겹치는 레이어)
        Image(
            painter = painterResource(id = R.drawable.foot),
            contentDescription = null,
            modifier = Modifier
                .width(416.95001.dp)
                .height(876.25.dp)
                .padding(start = 145.00015.dp, top = 483.00024.dp, end = 0.01566.dp, bottom = 0.41856.dp),
            contentScale = ContentScale.Crop // 화면에 꽉 차게 잘라서 표시
        )

        // 3. 로고와 텍스트 (가장 위에 중앙에 배치되는 레이어)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize() // Column이 Box 전체를 차지하도록 하여, 그 안에서 중앙 정렬
        ) {
            Image(
                painter = painterResource(id = R.drawable.mindwalk_logo),
                contentDescription = "MindWalk Logo",
                modifier = Modifier.size(120.dp) // 로고 크기 조절
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "MindWalk",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}