AppTitle "Dark Project 0"

Include "hud.bb"
Include "backdrop.bb"
Include "scenery.bb"
Include "screens.bb"
Include "player.bb"
Include "alien.bb"
Include "bullet.bb"
Include "explosion.bb"
Include "spark.bb"
Include "message.bb"
Include "mission.bb"
Include "swarm.bb"
Include "image.bb"
Include "utils.bb"
Include "data.bb"

Const WIDTH = 640, HEIGHT = 480
Const GMSTATE_TITLE = 1, GMSTATE_INGAME = 2, GMSTATE_ENDOFLEVEL = 3, GMSTATE_ENDGAME = 4
Const ONESECOND = 1000

Graphics WIDTH, HEIGHT

SetBuffer BackBuffer()

Global currentGameState
Global currentGameTime
Global heightIncHUD

;##################################################################################################
DisplayLoadingMessage()
DisplaySplashScreens()

SeedRnd MilliSecs()

Dim inGameMessage$(0)

LoadFonts()
LoadSounds()

Color 0, 180, 0
SetFont mainFont

LoadGameImages()

heightIncHUD = (HEIGHT - hud\height) - WAVE_STATBAR_THICKNESS

InitPlayer()

currentMission = 0
currentGameState = GMSTATE_TITLE
optionSelected = START_SELECTED

time=MilliSecs()

Repeat

	currentGameTime = MilliSecs()
	elapsed = currentGameTime - time
	
		Select currentGameState
		
			Case GMSTATE_TITLE
				DisplayStartScreen()
				
			Case GMSTATE_INGAME
				If elapsed > 25		;slowing down! - clamp update to 40 FPS (1000/40=25 millisecs)
					cnt = elapsed / 25
					For i = 1 To cnt
						UpdateGame()
					Next
					time = time + cnt * 25
				Else				;leave frame synced...
					UpdateGame()
					time = currentGameTime
				EndIf
				Cls
				RenderGame()
				Flip
				If KeyHit(1) Then ResetGame()
				
			Case GMSTATE_ENDOFLEVEL
				DisplayEndOfLevelScreen()
				
			Case GMSTATE_ENDGAME
				DisplayEndOfGameScreen()
				ResetGame()
				
		End Select
		
Forever

ExitProgram()

;##################################################################################################

Function UpdateGame()

	If player\isDead Then
		currentGameState = GMSTATE_ENDGAME
	Else
		tmp1 = UpdateBullets()
		
		If currentGameState = GMSTATE_INGAME Then UpdatePlayer()

		tmp2 = UpdateSwarms()
		tmp3 = UpdateAliens()
		tmp4 = UpdateExplosions()
		tmp5 = UpdateSparks()
		tmp6 = UpdateInGameMessages()

		UpdateShip()		
		UpdateScenery()

		If tmp1+tmp2+tmp3+tmp4+tmp5+tmp6 = 0 Then currentGameState = GMSTATE_ENDOFLEVEL
	EndIf
	
End Function

Function RenderGame()
	
	RenderBackdrop()
	RenderBullets()
	RenderPlayer()
	RenderAliens()
	RenderExplosions()
	RenderSparks()
	RenderScenery()
	RenderHUD()
	;Stop
	RenderInGameMessages()
	;Stop
End Function