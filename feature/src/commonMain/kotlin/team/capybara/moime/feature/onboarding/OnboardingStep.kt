package team.capybara.moime.feature.onboarding

import moime.feature.generated.resources.Res
import moime.feature.generated.resources.img_onboarding_1
import moime.feature.generated.resources.img_onboarding_2
import moime.feature.generated.resources.img_onboarding_3
import moime.feature.generated.resources.img_onboarding_4
import moime.feature.generated.resources.onboarding_desc_1
import moime.feature.generated.resources.onboarding_desc_2
import moime.feature.generated.resources.onboarding_desc_3
import moime.feature.generated.resources.onboarding_desc_4
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class OnboardingStep(
    val textRes: StringResource,
    val imageRes: DrawableResource
) {
    Step1(
        textRes = Res.string.onboarding_desc_1,
        imageRes = Res.drawable.img_onboarding_1
    ),
    Step2(
        textRes = Res.string.onboarding_desc_2,
        imageRes = Res.drawable.img_onboarding_2
    ),
    Step3(
        textRes = Res.string.onboarding_desc_3,
        imageRes = Res.drawable.img_onboarding_3
    ),
    Step4(
        textRes = Res.string.onboarding_desc_4,
        imageRes = Res.drawable.img_onboarding_4
    );
}
