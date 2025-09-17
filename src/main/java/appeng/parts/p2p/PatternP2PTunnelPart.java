package appeng.parts.p2p;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import net.minecraft.server.level.ServerLevel;

import appeng.api.config.Actionable;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.stacks.AEKey;
import appeng.core.AppEng;
import appeng.helpers.patternprovider.PatternProviderTarget;
import appeng.items.parts.PartModels;
import appeng.me.helpers.MachineSource;

public class PatternP2PTunnelPart extends P2PTunnelPart<PatternP2PTunnelPart> {

    private static final P2PModels MODELS = new P2PModels(AppEng.makeId("part/p2p/p2p_tunnel_pattern"));

    private final PatternProviderTarget input = new InputHandler();
    private final PatternProviderTarget output = new OutputHandler();
    private final PatternProviderTarget empty = new EmptyHandler();

    public PatternP2PTunnelPart(IPartItem<?> partItem) {
        super(partItem);
    }

    @PartModels
    public static List<IPartModel> getModels() {
        return MODELS.getModels();
    }

    @Override
    public IPartModel getStaticModels() {
        return MODELS.getModel(this.isPowered(), this.isActive());
    }

    public PatternProviderTarget getExposedTarget() {
        return isOutput() ? output : input;
    }

    protected PatternProviderTarget getInputTarget() {
        var input = getInput();
        if (input == null) {
            return empty;
        }
        var target = input.getAdjacentTarget();
        return target == null ? empty : target;
    }

    private PatternProviderTarget getAdjacentTarget() {
        if (!(getLevel() instanceof ServerLevel serverLevel)) {
            return null;
        }

        var neighbor = getBlockEntity().getBlockPos().relative(getSide());
        var be = serverLevel.getBlockEntity(neighbor);
        return PatternProviderTarget.get(serverLevel, neighbor, be, getSide().getOpposite(), new MachineSource(this));
    }

    @Override
    public Stream<PatternP2PTunnelPart> getOutputStream() {
        return super.getOutputStream().limit(32);
    }

    private class InputHandler implements PatternProviderTarget {

        @Override
        public long insert(AEKey what, long amount, Actionable type) {
            var out = getOutputs();
            int count = out.size();
            if (count == 0 || amount == 0) {
                return 0;
            }

            long amountPerOutput = amount / count;
            long remaining = amountPerOutput == 0 ? amount : amount % amountPerOutput;
            long totalInserted = 0;

            for (var p2p : out) {
                var target = p2p.getAdjacentTarget();
                if (target == null) {
                    continue;
                }
                long toSend = amountPerOutput + remaining;
                long inserted = target.insert(what, toSend, type);
                remaining = toSend - inserted;
                totalInserted += inserted;
            }

            if (type == Actionable.MODULATE) {
                deductTransportCost(totalInserted, what.getType());
            }

            return totalInserted;
        }

        @Override
        public boolean containsPatternInput(Set<AEKey> patternInputs) {
            for (var p2p : getOutputs()) {
                var target = p2p.getAdjacentTarget();
                if (target != null && target.containsPatternInput(patternInputs)) {
                    return true;
                }
            }
            return false;
        }
    }

    private class OutputHandler implements PatternProviderTarget {

        @Override
        public long insert(AEKey what, long amount, Actionable type) {
            var target = getInputTarget();
            long inserted = target.insert(what, amount, type);
            if (type == Actionable.MODULATE) {
                deductTransportCost(inserted, what.getType());
            }
            return inserted;
        }

        @Override
        public boolean containsPatternInput(Set<AEKey> patternInputs) {
            return getInputTarget().containsPatternInput(patternInputs);
        }
    }

    private static class EmptyHandler implements PatternProviderTarget {

        @Override
        public long insert(AEKey what, long amount, Actionable type) {
            return 0;
        }

        @Override
        public boolean containsPatternInput(Set<AEKey> patternInputs) {
            return false;
        }
    }
}
