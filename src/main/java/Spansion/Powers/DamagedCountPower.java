package Spansion.Powers;

import Spansion.Spansion;
import Spansion.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Spansion.Spansion.makePowerPath;

public class DamagedCountPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = Spansion.makeID(DamagedCountPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    private int damageTaken = 0;
    public  boolean PlayerTurn = true;

    public DamagedCountPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if(PlayerTurn) {
            damageTaken++;
            Spansion.logger.info("Damage taken on player's turn: " + damageTaken);
            updateDescription();
        }
        return super.onLoseHp(damageAmount);
    }

    @Override
    public void atStartOfTurn() {
        PlayerTurn = true;
        Spansion.logger.info("Start of Turn.  Players: " + PlayerTurn);
        super.atStartOfTurn();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        PlayerTurn = false;
        Spansion.logger.info("End of Turn.  Players: " + PlayerTurn);
        super.atEndOfTurn(isPlayer);
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + damageTaken + DESCRIPTIONS[1];

    }

    @Override
    public AbstractPower makeCopy() {
        return new DamagedCountPower(owner, source, amount);
    }

    public int TimesDamaged(){
        return damageTaken;
    }
}