package com.thoughtworks.sea.token

import com.thoughtworks.sea.model.TokenResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SCTokenParserTest {
    @Test
    fun shouldReturnParseTokenResultGivenCorrectToken() {
        val scTokenParser = SCTokenParser()
        val mockIdToken =
            "eyJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwiYWxnIjoiUlNBLU9BRVAiLCJraWQiOiJiemEwZFhmNkZqbGExRlFyVkttQVR1WmI5LTRNOTBMeER1ZjN1akxZYnFnIn0.kB0SwnoViom7QEJU5hMedQ2T62ofPiNiYFY8aaoclylbX4t-DIsScUgbNUICkbEx0KjCpOY1w2YNUdRSkLH6Ve0Nm0ciSzxHs_r33iR6IHgGpoL71tfOxlBo7TzswZITMuIEYYz9riKjaHoprxt2F2LXHjbU4QJGaDXCuNncYMemA87Ba5_9vpSzpsCxFcRIsOcTO0dspTpK457y8OIeYlL-C2kDCcNHeCZa9RMMRMXFzmPkjKQe2x19NdtJuxNCrxVxgZT-1pD1vU0WHohknwz78qa4Wo9Ze2V27WMQPjwPY8NZcWZBaAxN1LORV4fPS9D_-I-vv3-TlwhqZgd2HA.aM6YF9mo_GhGDgqHhIM62A.4JVqNmif7Owsngn6qNjyLufKYxVOXTy1DloVulSSyE2W5qX7xQcv3ye6krNJLNb7MDPnDtSR133ek7GCHYRxgmnO2-68sbgNYhfJVbDUPcGTnAT3nhyTHf10kPDCKvrnK5jZcZKEnNFpcMnw53xV4ofkn7nAtaEYl4h6T4VHsXjjBaLLny8jUXdB1zuPfs0Zd-KSg1tp4DT5r2Q99MxXRoDUzVKSA7evTw_B8WeANEtbj_5TU8e0yp3npql657j52gOMBxETfZk1jOWo34DVnmNowH3bTaQen4Auqiulx1GtjYVvqDxhncQTWr_dARXn_kxMBWC-Sn3viHH7j0oi2sFIPamXY3GXEKJ26N7T5abrOHAxFnJogtXbscaFcmx8l0xGT94fMryjsGvlPERpgee8q0_1_-o2TEwGcvYHeWPG5Agcs5PvFRExzoJBaz_Qhd9thADzS2T4tEOO_tcaRelA-nKU_4-dVXHajwd2Eck_JrqH-rrun5qA_60OSQFpzOm04SOlcowUke4ybud6IsxZEhtpAnEmcKzV_kc3Hwn3JW2UClqGRv8qT_UwS4BsKwVuzyEd8U6bR1jiq46ncm0XbuFh35XgG1vIxeepYPPWTaWvw70U5fcNqg2FaYbrqWSmtQradhPYRS1qaQS43qVWXBcWb7tRFxbuN8UY18O4qhmxtyx5_YISphsmbBSJhczBs7ep4Zgxe6N6pz26GaW-I4xAk1PA2_hZtxxcXBSSHxaJEz37nS-bkORShX9pOZubscPjx41vKPy1yMniCsLmufY03nqqkljW3ETqnWo97LkJhc4F69E4WAYg6UorvVLWbFcH6xDkVKWxy4-wmqW6M4tSzAhEm4z-QnNfPPXRdWcl4DQHmvuH5Dag3jhKLy9CECx071kfsZJf2YO0UpzyCsUpOw0cTQGVnt8e5G4.Y0brj0469jLvMDhMUyrZmw"
        val mockToken = TokenResponse(
            "accessToken",
            "refreshToken",
            mockIdToken,
            "bearer",
            200,
            "openid"
        )
        assertEquals("S3000024B", scTokenParser.parseToken(mockToken).nricNumber)
    }
}
