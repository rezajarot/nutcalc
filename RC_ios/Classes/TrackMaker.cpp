
#include "TrackMaker.h"

#include <math.h>

#ifndef Sqr
#define	Sqr(x) ((x)*(x))
#endif

int Abs( int nVal )
{
	return nVal < 0 ? -(nVal) : nVal;
}

#ifndef PI
#define PI 3.1415926535897932384626433832795
#endif

LevelInformation LEVEL_INFO_ARRAY_FOR_IPHONE[LV_NUM] = 
{
	//Level1
	{
		158, 430, 255, 240, 158, 50, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 270, 100, 206, 5, 1, 0.7f, 0.3f, 40, 140
	},
	//Level2
	{
		160, 430, 240, 223, 160, 20, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 380, 80, 210, 5, 1, 0.7f, 0.2f, 40, 140
	},
	//Level3
	{
		160, 430, 137, 133, 160, 15, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 250, 35, 126, 5, 1, 0.7f, 0.1f, 50, 130
	}
};


TrackMaker::TrackMaker()
{
	m_nCurrentFrameIndex = 0;
	SetWindSpeed(1, 0);
	SetMachine(MACHINE_IPHONE);
}

TrackMaker::~TrackMaker()
{

}

void TrackMaker::SetMachine(int nMachine)
{
	m_nMachine = nMachine;
}

LevelInformation* TrackMaker::GetLevelInfo()
{
	return &m_LevelInfo;
}

void TrackMaker::SetLevel(int nLevelNum)
{
	float fScaleX = 1, fScaleY = 1;
	
	if (m_nMachine != MACHINE_IPHONE)
	{
		fScaleX = (float)SCALEX_IPHONE_IPAD;
		fScaleY = (float)SCALEY_IPHONE_IPAD;
	}
	
	m_LevelInfo.nPaperInitX = (int)(LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nPaperInitX * fScaleX);
	m_LevelInfo.nPaperInitY = (int)(LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nPaperInitY * fScaleY);
	m_LevelInfo.nBinBottomY = (int)(LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nBinBottomY * fScaleY);
	m_LevelInfo.nBinTopY = (int)(LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nBinTopY * fScaleY);
	m_LevelInfo.nBinCenterX = (int)(LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nBinCenterX * fScaleX);
	m_LevelInfo.nBinWidth = (int)(LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nBinWidth * fScaleX);
	m_LevelInfo.nUpFrameNum = UP_FRAME_NUM;
	m_LevelInfo.nDownFrameNum = DOWN_FRAME_NUM;
	m_LevelInfo.nDropFrameNum = DROP_FRAME_NUM;
	m_LevelInfo.nBackFrameNum = BACK_FRAME_NUM;
	m_LevelInfo.nFlyRight = (int)(LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nFlyRight * fScaleX);
	m_LevelInfo.nFlyFloor = (int)(LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nFlyFloor * fScaleX);
	m_LevelInfo.nFlyTop = (int)(LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nFlyTop * fScaleY);
	m_LevelInfo.nFlyBackTop = (int)(LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nFlyBackTop * fScaleY);
	m_LevelInfo.nFlyBackFromFloor = (int)(LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nFlyBackFromFloor * fScaleY);
	m_LevelInfo.rInitScale = LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].rInitScale;
	m_LevelInfo.rTopScale = LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].rTopScale;
	m_LevelInfo.rLastScale = LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].rLastScale;
	m_LevelInfo.nStartAngle = LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nStartAngle;
	m_LevelInfo.nEndAngle = LEVEL_INFO_ARRAY_FOR_IPHONE[nLevelNum].nEndAngle;
}

int TrackMaker::SetThrowAngle(float rThrowAngle)
{
	m_rThrowAngle = rThrowAngle;
	
	int nAngle = m_rThrowAngle * 180 / PI;
	if (nAngle < m_LevelInfo.nStartAngle || nAngle > m_LevelInfo.nEndAngle)
		return 0;
	
	CalcTrack();
	
	return 1;
}

void TrackMaker::CalcTrack()
{
	int nFrameIndex;
	float rUpAy, rDownAy, rAx;
	float rV0y, rV0x;
	float rLastV0x;
	int nPaperX, nPaperY;
	int nLastPaperX, nLastPaperY;
	float rLastScale, rScale;
	
	float rInterVal = (float)UPTIME / UP_FRAME_NUM;
	
	m_nCurrentFrameIndex = 0;
	m_nRealFrameNum = 0;
	m_fSuccess = false;
	
	rUpAy = (float)2 * (m_LevelInfo.nPaperInitY - m_LevelInfo.nFlyTop) / Sqr(UPTIME);
	rAx = (float)(m_LevelInfo.nFlyRight - m_LevelInfo.nBinCenterX) * 2 * m_rWindSpeed * m_nWindDrect / (MAX_WIND);
	rV0y = rUpAy * UPTIME;
	rV0x = rV0y * (float)cos(m_rThrowAngle) / sin(m_rThrowAngle);
	rScale = m_LevelInfo.rInitScale;
	m_nCurrentRoundIndex = -1;
	
	for (nFrameIndex = 0; nFrameIndex < UP_FRAME_NUM; nFrameIndex++)
	{
		int nDeltaX, nDeltaY;
		float rCurrentTime;
		
		rCurrentTime = rInterVal * nFrameIndex;
		nDeltaY = (int)(rV0y * rCurrentTime - rUpAy * Sqr(rCurrentTime) / 2); 
		nPaperY = (int)(m_LevelInfo.nPaperInitY - nDeltaY);
		
		if (rV0x * m_nWindDrect > 0)
		{
			nDeltaX = (int)(rV0x * rCurrentTime + rAx * 0.3 * Sqr(rCurrentTime) / 2);
		}
		else
		{
			nDeltaX = (int)(nDeltaY * cos(m_rThrowAngle) / sin(m_rThrowAngle));
		}
		
		nPaperX = m_LevelInfo.nPaperInitX + nDeltaX;
		if (m_nCurrentFrameIndex != 18 && m_nCurrentFrameIndex != 20)
		{
			AddRoundIndex(m_nCurrentRoundIndex);
		}
		
		m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
		m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
		m_rScale[m_nCurrentFrameIndex] = rScale;
		m_nState[m_nCurrentFrameIndex] = STATE_NONE;
		m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
		nLastPaperX = nPaperX;
		nLastPaperY = nPaperY;
		rLastScale = rScale;
		rScale -= (m_LevelInfo.rInitScale - m_LevelInfo.rTopScale) / UP_FRAME_NUM;
		m_nCurrentFrameIndex++;
		rLastV0x = rV0x;
	}

	for (nFrameIndex = 0; nFrameIndex < TOP_FRAME_NUM; nFrameIndex++)
	{
		if (rV0x * m_nWindDrect > 0)
		{
			nPaperX += (int)((rV0x * rInterVal + rAx * Sqr(rInterVal) / 2));
		}
		if (((m_nCurrentFrameIndex == 22) || (m_nCurrentFrameIndex == 24)) && (rV0x * m_nWindDrect < 0))
		{
			rScale -= (m_LevelInfo.rInitScale - m_LevelInfo.rTopScale) / UP_FRAME_NUM;
		}
		if ((m_nCurrentFrameIndex == 23)||((m_nCurrentFrameIndex == 25)))
		{
			AddRoundIndex(m_nCurrentRoundIndex);
		}
		
		m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
		m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
		m_rScale[m_nCurrentFrameIndex] = rScale;
		m_nState[m_nCurrentFrameIndex] = STATE_NONE;
		m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
		rLastScale = rScale;
		nLastPaperX = nPaperX;
		rLastV0x += rAx * rInterVal;
		m_nCurrentFrameIndex++;

	}
	
	rInterVal = (float)DOWNTIME / DOWN_FRAME_NUM;
	rDownAy = (float)2 * (m_LevelInfo.nFlyFloor - m_LevelInfo.nFlyTop) / Sqr(DOWNTIME);
	
	for (nFrameIndex = 0; nFrameIndex < DOWN_FRAME_NUM; nFrameIndex++)
	{
		int nDeltaX, nDeltaY;
		float rCurrentTime;
		
		rCurrentTime = rInterVal * nFrameIndex;

		nDeltaY = (int)(rDownAy * Sqr(rCurrentTime) / 2); 
		nPaperY = nLastPaperY + nDeltaY;

		if (rAx == 0)
		{
			nDeltaX = (int)(nDeltaY * cos(m_rThrowAngle) / sin(m_rThrowAngle));
		}
		else
		{
			nDeltaX = (int)(rAx * Sqr(rCurrentTime) / 2);
			if (rV0x * m_nWindDrect > 0)
			{
				nDeltaX += (int)(rLastV0x * rCurrentTime);
			}
		}
		
		nPaperX = nLastPaperX + nDeltaX;
		rScale -= (rLastScale - m_LevelInfo.rLastScale) / DOWN_FRAME_NUM;

		if (m_nCurrentFrameIndex != 28 && m_nCurrentFrameIndex != 30)
		{
			AddRoundIndex(m_nCurrentRoundIndex);
		}

		m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
		m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
		m_rScale[m_nCurrentFrameIndex] = rScale;
		m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
		m_nState[m_nCurrentFrameIndex] = STATE_NONE;
		m_nCurrentFrameIndex++;
		
		if ((nPaperY >= m_LevelInfo.nBinTopY) && (m_nTrackY[m_nCurrentFrameIndex - 2] < m_LevelInfo.nBinTopY))
		{
			int nPassBinPaperX, nPassedX, nPassedY;
			
			nPassedX = m_nTrackX[m_nCurrentFrameIndex - 2];
			nPassedY = m_nTrackY[m_nCurrentFrameIndex - 2];
			nPassBinPaperX = (float)((m_LevelInfo.nBinTopY - nPassedY) * (nPaperX - nPassedX)) / (nPaperY - nPassedY) + nPassedX;

			if ((Abs(nPassBinPaperX - m_LevelInfo.nBinCenterX) < m_LevelInfo.nBinWidth * 0.8f))
			{
				if ((Abs(nPassBinPaperX - m_LevelInfo.nBinCenterX) < m_LevelInfo.nBinWidth * 0.5f))
				{
					m_nState[m_nCurrentFrameIndex - 1] = STATE_IN;
				}
				else
				{
					m_nState[m_nCurrentFrameIndex - 1] = STATE_SIDE_IN;
				}
				
				nPaperY = m_LevelInfo.nBinTopY;
				nPaperX = nPassBinPaperX;
				
				m_nTrackY[m_nCurrentFrameIndex - 1] = nPaperY;
				m_nTrackX[m_nCurrentFrameIndex - 1] = nPaperX;
				
				CalcDropTrack(nPaperX, nPaperY, rScale);
				
				m_fSuccess = true;
				
				break;
			}
			
			if ((Abs(nPassBinPaperX - m_LevelInfo.nBinCenterX) >= m_LevelInfo.nBinWidth * 0.8f) &&
				(Abs(nPassBinPaperX - m_LevelInfo.nBinCenterX) < m_LevelInfo.nBinWidth * 1.1f))//1.1
			{
				int nMode;
				if (nPaperX < m_LevelInfo.nBinCenterX)
				{
					nMode = 1;
				}
				else
				{
					nMode = -1;
				}
				
				m_nState[m_nCurrentFrameIndex - 1] = STATE_RING_IN;
				
				nPaperY = m_LevelInfo.nBinTopY;
				nPaperX = nPassBinPaperX;
				
				m_nTrackY[m_nCurrentFrameIndex - 1] = nPaperY;
				m_nTrackX[m_nCurrentFrameIndex - 1] = nPaperX;
				
				CalcBackTrack(nPaperX, nPaperY, rScale, nMode);
				
				m_fSuccess = true;
				
				break;
			}
			
			if ((((Abs(nPassBinPaperX - m_LevelInfo.nBinCenterX) >= m_LevelInfo.nBinWidth * 1.1f) &&//1.1
				(Abs(nPassBinPaperX - m_LevelInfo.nBinCenterX) < m_LevelInfo.nBinWidth * 1.3f))))//1.3
			{
				int nMode;
				if (nPaperX < m_LevelInfo.nBinCenterX)
				{
					nMode = -1;
				}
				else
				{
					nMode = 1;
				}
				
				m_nState[m_nCurrentFrameIndex - 1] = STATE_RING_OUT;
				
				nPaperY = m_LevelInfo.nBinTopY;
				nPaperX = nPassBinPaperX;
				
				m_nTrackY[m_nCurrentFrameIndex - 1] = nPaperY;
				m_nTrackX[m_nCurrentFrameIndex - 1] = nPaperX;
				
				CalcRingOutTrack(nPaperX, nPaperY, rScale, nMode);
				
				break;
			}
		}
	}
	
	if (!m_fSuccess)
	{
		m_nState[m_nCurrentFrameIndex - 1] = STATE_OUT;
		
		nPaperY = m_LevelInfo.nFlyFloor;
		
		for (nFrameIndex = 0; nFrameIndex < BACK_FROM_FLOOR_FRAME_NUM; nFrameIndex++)
		{
			if (nFrameIndex == 0)
			{
				nPaperY -= (int)(10 * m_LevelInfo.rLastScale);
				nPaperX += (int)(rAx * 0.02 * m_LevelInfo.rLastScale);
			}
			else if ((nFrameIndex >=2) && (nFrameIndex < BACK_FROM_FLOOR_FRAME_NUM))
			{
				nPaperY += (int)(7 * m_LevelInfo.rLastScale);
				nPaperX += (int)(rAx * 0.01 * m_LevelInfo.rLastScale);
			}
			
			/*m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
			m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
			m_rScale[m_nCurrentFrameIndex] = rScale;
			m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
			m_nState[m_nCurrentFrameIndex] = STATE_NONE;
			m_nCurrentFrameIndex++;*/
		}
	}
	/*
	for (nFrameIndex = 0; nFrameIndex < STOP_FRAME_NUM; nFrameIndex++)
	{
		m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
		m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
		m_rScale[m_nCurrentFrameIndex] = rScale;
		m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
		m_nState[m_nCurrentFrameIndex] = STATE_NONE;
		m_nCurrentFrameIndex++;
	}
	*/
	m_nRealFrameNum = m_nCurrentFrameIndex;
}

void TrackMaker::CalcDropTrack(int& nPaperX, int& nPaperY, float& rScale)
{
	float rDropSpeedX = (m_LevelInfo.nBinCenterX - nPaperX) / DROPTIME;
	float rDropSpeedY = (m_LevelInfo.nBinBottomY - nPaperY) / DROPTIME;
	float rInterVal = (float)DROPTIME / DROP_FRAME_NUM;
	//float rInitScale = rScale;
	int nStartX, nStartY;
	
	nStartX = nPaperX;
	nStartY = nPaperY;
	
	for (int nFrameIndex = 1; nFrameIndex <= DROP_FRAME_NUM; nFrameIndex++)
	{
		nPaperX = nStartX + (int)((rInterVal * nFrameIndex) * rDropSpeedX);
		nPaperY = nStartY + (int)((rInterVal * nFrameIndex) * rDropSpeedY);
		//rScale -= (rInitScale - m_LevelInfo.rLastScale) / DROP_FRAME_NUM;
		
		m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
		m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
		m_rScale[m_nCurrentFrameIndex] = rScale;
		m_nState[m_nCurrentFrameIndex] = STATE_NONE;
		if (nFrameIndex == 1)
			m_nState[m_nCurrentFrameIndex] = STATE_IN;
		m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
		
		m_nCurrentFrameIndex++;
	}
}

void TrackMaker::CalcBackTrack(int& nPaperX, int& nPaperY, float& rScale, int nMode)
{
	float rAyUp = (float)2 * (m_LevelInfo.nBinTopY - m_LevelInfo.nFlyBackTop) / Sqr(BACKTIME / 2);
	float rVY0 = (float)(rAyUp * BACKTIME / 2);
	int nDeltaY;
	float rInterVal = (float)(BACKTIME / 2) / BACK_HALF_FRAME_NUM;
	
	int nStartX = nPaperX;
	
	int nFrameIndex;
	
	for (nFrameIndex = 0; nFrameIndex < BACK_HALF_FRAME_NUM; nFrameIndex++)
	{
		float rCurrentTime = (nFrameIndex + 1) * rInterVal;
		nPaperX = nStartX + nMode * (m_LevelInfo.nBinWidth * (nFrameIndex + 1) / 2 / BACK_HALF_FRAME_NUM);
		nDeltaY = (int)(rVY0 * rCurrentTime - rAyUp * Sqr(rCurrentTime) / 2);
		nPaperY = m_LevelInfo.nBinTopY - nDeltaY;
		
		m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
		m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
		m_rScale[m_nCurrentFrameIndex] = rScale;
		m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
		
		m_nState[m_nCurrentFrameIndex] = STATE_NONE;
		m_nCurrentFrameIndex++;
	}

	int nDownFrameNum = BACK_FRAME_NUM - BACK_HALF_FRAME_NUM;
	//int nInBinFrameNum = nDownFrameNum - BACK_HALF_FRAME_NUM;
	
	//float rScaleInterval = (rScale - m_LevelInfo.rLastScale) / nInBinFrameNum;
	nStartX = nPaperX;
	
	for (nFrameIndex = 0; nFrameIndex < nDownFrameNum; nFrameIndex++)
	{
		float rCurrentTime = (nFrameIndex + 1) * rInterVal;
		nDeltaY = (int)(rAyUp * Sqr(rCurrentTime));
		nPaperY = m_LevelInfo.nFlyBackTop + nDeltaY;

		if (nFrameIndex < BACK_HALF_FRAME_NUM)
		{
			nPaperX = nStartX + nMode * (m_LevelInfo.nBinWidth * (nFrameIndex + 1) / 2 / nDownFrameNum);
		}
		else
		{
			//rScale -= rScaleInterval;
		}
		
		if (nPaperY > m_LevelInfo.nBinBottomY)
			break;
		
		m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
		m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
		m_rScale[m_nCurrentFrameIndex] = rScale;
		m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
		m_nState[m_nCurrentFrameIndex] = STATE_NONE;
		m_nCurrentFrameIndex++;
	}
	
	nPaperX = m_LevelInfo.nBinCenterX;
	
	CalcDropTrack(nPaperX, nPaperY, rScale);
}

void TrackMaker::CalcRingOutTrack(int& nPaperX, int& nPaperY, float& rScale, int nMode)
{
	float rAyUp = (float)(2 * (m_LevelInfo.nBinTopY - m_LevelInfo.nFlyBackTop) / Sqr(BACKTIME / 2));
	float rVY0 = (float)(rAyUp * BACKTIME / 2);
	int nDeltaY, nDeltaX;
	int nInitX = nPaperX;
	//float rInitScale = rScale;
	
	float rInterVal = (float)(BACKTIME / 2) / BACK_HALF_FRAME_NUM;
	
	for (int nFrameIndex = 0; nFrameIndex < RING_OUT_FRAME_NUM; nFrameIndex++)
	{
		float rCurrentTime = (nFrameIndex + 1) * rInterVal;
		
		nDeltaX = (int)(nMode * (m_LevelInfo.nBinWidth / BACKTIME) * rCurrentTime);
		nPaperX = nInitX + nDeltaX;
		nDeltaY = (int)(rVY0 * rCurrentTime - rAyUp * Sqr(rCurrentTime) / 2);
		nPaperY = m_LevelInfo.nBinTopY - nDeltaY;
		
		if (nFrameIndex > BACKTIME / 2)
		{
			//rScale -= (rInitScale - m_LevelInfo.rLastScale) / (RING_OUT_FRAME_NUM - BACK_HALF_FRAME_NUM);
		}
		
		if (nPaperY > m_nTrackY[m_nCurrentFrameIndex - 1])
			break;
		
		m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
		m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
		m_rScale[m_nCurrentFrameIndex] = rScale;
		m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
		m_nState[m_nCurrentFrameIndex] = STATE_NONE;
		m_nCurrentFrameIndex++;
		
		if (nPaperY > m_LevelInfo.nBinBottomY)
		{
			nPaperY = m_LevelInfo.nBinBottomY;
			m_nTrackY[m_nCurrentFrameIndex - 1] = nPaperY;
			
			break;
		}
	}
	
	rInterVal = (float)DOWNTIME / DOWN_FRAME_NUM;
	int nLastPaperY = m_nTrackY[m_nCurrentFrameIndex - 1];
	float rDownAy = (float)2 * (m_LevelInfo.nFlyFloor - m_LevelInfo.nFlyBackTop) / Sqr(DOWNTIME);
	
	for (int nFrameIndex = 0; nFrameIndex < DOWN_FRAME_NUM; nFrameIndex += 3)
	{
		int nDeltaY;
		float rCurrentTime = (nFrameIndex + RING_OUT_FRAME_NUM) * rInterVal;
		
		nDeltaX = (int)(nMode * (m_LevelInfo.nBinWidth / DOWNTIME) * rCurrentTime);
		nPaperX = nInitX + nDeltaX;
		nDeltaY = (int)(rDownAy * Sqr(rInterVal * nFrameIndex) / 2); 
		nPaperY = nLastPaperY + nDeltaY;
		
		m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
		m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
		m_rScale[m_nCurrentFrameIndex] = rScale;
		m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
		m_nState[m_nCurrentFrameIndex] = STATE_NONE;
		m_nCurrentFrameIndex++;
	}
}

bool TrackMaker::GetTrack(int nFrameNum, int& nPaperX, int &nPaperY, float& rScale, int& nRotate, int& nState)
{
	if (nFrameNum >= m_nRealFrameNum)
	{
		return false;
	}
	
	nRotate = 1;
	nPaperX = m_nTrackX[nFrameNum];
	nPaperY = m_nTrackY[nFrameNum];
	rScale = m_rScale[nFrameNum];
	nState = m_nState[nFrameNum];
	nRotate = m_nRound[nFrameNum];
	
	return true;
}

void TrackMaker::SetWindSpeed(int nDirect, float rSpeed)
{
	m_nWindDrect = nDirect;
	m_rWindSpeed = rSpeed;
}

int TrackMaker::GetRealFrameNum()
{
	return m_nRealFrameNum;
}

bool TrackMaker::GetSuccess()
{
	return m_fSuccess;
}

void TrackMaker::AddRoundIndex(int& nRoundIndex) {
	int nRoundLimit;
	if (m_nMachine == MACHINE_IPHONE)
	{
		nRoundLimit = IPHONE_ROUND_LIMIT;
	}
	else
	{
		nRoundLimit = IPAD_ROUND_LIMIT;
	}
	nRoundIndex++;
	if (nRoundIndex == nRoundLimit)
	{
		nRoundIndex = 0;
	}
}
