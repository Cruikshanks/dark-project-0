Const BACKDROP_BOTTOM_LAYER = 0, BACKDROP_TOP_LAYER = 1
Const SCREEN_START = 0, SCREEN_DIED = 1, SCREEN_LOST = 2, SCREEN_WON = 3

Const P_STD_BLT = 0, A_STD_BLT = 1
Const P_BOMB_BLT = 2, A_BOMB_BLT = 3
Const P_HOMING_BLT = 4, A_HOMING_BLT = 5

Type ImageData
	Field filename$
	Field width			;if animImage, this is frame width
	Field height		;if animInage, this is frame height
	Field numOfFrames
	Field startFrame
	Field image
End Type

Global hud.ImageData = New ImageData
Global backdrop.ImageData = New ImageData
Global screens.ImageData = New ImageData
Global endgameMessage.ImageData = New ImageData
Global scenery.ImageData = New ImageData
Global characters.ImageData = New ImageData

Global explosion.ImageData = New ImageData
Global spark.ImageData = New ImageData

Global plyFired.ImageData = New ImageData
Global plyNotFired.ImageData = New ImageData

Global stdAlien.ImageData = New ImageData
Global bombAlien.ImageData = New ImageData
Global aceAlien.ImageData = New ImageData

Global bullets.ImageData = New ImageData

Function LoadGameImage(i.ImageData,fname$,w,h,numOfFrames,stFrame,r,g,b)

	i\filename = fname$
	i\numOfFrames = numOfFrames
	i\startFrame = stFrame
	
	If numOfFrames > 1 Then 
		i\image = LoadAnimImage(fname$,w,h,0,numOfFrames)
		i\width = w
		i\height = h
	Else
		i\image = LoadImage(fname$)
		i\width = ImageWidth(i\image)
		i\height = ImageHeight(i\image)
	EndIf

	CheckImage(i\image, fname$)
	MaskImage i\image, r, g, b
	
End Function

Function LoadGameImages()

	LoadGameImage(hud,"graphics\hud.bmp",0,0,1,0,255,0,255)
	
	LoadGameImage(screens,"graphics\screens.bmp",640,480,4,0,0,0,0)
	
	LoadGameImage(characters,"graphics\characters.bmp",48,48,8,0,255,0,255)
	CreateCharacterImages(255,0,255)
	
	LoadGameImage(explosion,"graphics\kaboom.bmp",60,48,6,0,0,0,0)
	LoadGameImage(spark,"graphics\spark.bmp",16,16,3,0,255,0,255)

	LoadGameImage(plyFired,"graphics\plyshp01b.bmp",64,64,15,6,255,0,255)
	LoadGameImage(plyNotFired,"graphics\plyshp01a.bmp",64,64,15,6,255,0,255)
	LoadGameImage(stdAlien,"graphics\alienstd.bmp",32,32,20,0,255,0,255)
	LoadGameImage(bombAlien,"graphics\alienbomber.bmp",32,32,20,0,255,0,255)
	LoadGameImage(aceAlien,"graphics\alienace.bmp",32,32,20,0,255,0,255)

	LoadGameImage(bullets,"graphics\bullets.bmp",16,16,6,0,255,0,255)
	
End Function

Function CreateCharacterImages(r,g,b)

	Dim character%(characters\numOfFrames - 1)
	
	For i = 0 To characters\numOfFrames - 1
		image = CreateImage(characters\width, characters\height)
		SetBuffer ImageBuffer(image)
		DrawImage characters\image, 0, 0, i
		MaskImage image, r, g, b
		character%(i) = image
	Next
	
	SetBuffer BackBuffer()
	
End Function