package edu.umd.cs.findbugs.util;

import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.io.Serializable;
import javax.annotation.concurrent.Immutable;

import org.junit.Assert;
import org.junit.Test;

@Immutable
class Annotated {
    int n;

    Annotated(int n) {
        this.n = n;
    }

    // False setter
    void set(int n) {
        System.out.println("This is not a setter. So we do not set n to " + n + ".");
    }
}

public class MutableClassesTest {
    @Test
    public void TestKnownMutable() {
        Assert.assertTrue(MutableClasses.mutableSignature("Ljava/util/Date;"));
    }

    @Test
    public void TestKnownImmutablePackage() {
        Assert.assertFalse(MutableClasses.mutableSignature("Ljava/time/LocalTime;"));
    }

    @Test
    public void TestKnownImmutable() {
        Assert.assertFalse(MutableClasses.mutableSignature("Ljava/lang/String;"));
    }

    @Test
    public void TestArray() {
        Assert.assertTrue(MutableClasses.mutableSignature("[I"));
    }

    @Test
    public void TestAnnotatedImmutable() {
        Assert.assertFalse(MutableClasses.mutableSignature("Ledu/umd/cs/findbugs/util/Annotated;"));
    }

    public static class Mutable {
        private int n;

        public Mutable(int n) {
            this.n = n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public int getN() {
            return n;
        }
    }

    @Test
    public void TestMutable() {
        Assert.assertTrue(MutableClasses.mutableSignature("Ledu/umd/cs/findbugs/util/MutableClassesTest$Mutable;"));
    }

    public static class Immutable {
        private final int n;
        private static Immutable immutable;

        public Immutable(int n) {
            this.n = n;
        }

        public int getN() {
            return n;
        }

        public Immutable setN(int n) {
            return new Immutable(n);
        }

        public static Immutable getImmutable() {
            return immutable;
        }

        public static void setImmutable(Immutable imm) {
            immutable = imm;
        }
    }

    @Test
    public void TestImmutable() {
        Assert.assertFalse(MutableClasses.mutableSignature("Ledu/umd/cs/findbugs/util/MutableClassesTest$Immutable;"));
    }

    public static final class MutableWriteReplace {
        Object writeReplace() throws ObjectStreamException {
            return null;
        }
    }

    @Test
    public void TestMutableWriteReplace() {
        Assert.assertTrue(MutableClasses.mutableSignature(
                "Ledu/umd/cs/findbugs/util/MutableClassesTest$MutableWriteReplace;"));
    }

    public static final class ImmutableWriteReplace implements Serializable {
        Object writeReplace() throws ObjectStreamException {
            return null;
        }
    }

    @Test
    public void TestImmutableWriteReplace() {
        Assert.assertFalse(MutableClasses.mutableSignature(
                "Ledu/umd/cs/findbugs/util/MutableClassesTest$ImmutableWriteReplace;"));
    }

    public static final class MutableWriteExternal {
        void writeExternal(ObjectOutput out) {
            // Does not matter
        }
    }

    public static class MutableWriteExternalSig implements Externalizable {
        @Override
        public void writeExternal(ObjectOutput out) {
            // Does not matter
        }

        @Override
        public void readExternal(ObjectInput in) {
            // Does not matter
        }

        void writeExternal() {
            // Does not match signature
        }
    }

    @Test
    public void TestMutableWriteExternal() {
        Assert.assertTrue(MutableClasses.mutableSignature(
                "Ledu/umd/cs/findbugs/util/MutableClassesTest$MutableWriteExternal;"));
        Assert.assertTrue(MutableClasses.mutableSignature(
                "Ledu/umd/cs/findbugs/util/MutableClassesTest$MutableWriteExternalSig;"));
    }

    public static final class ImmutableWriteExternal implements Externalizable {
        @Override
        public void writeExternal(ObjectOutput out) {
            // Does not matter
        }

        @Override
        public void readExternal(ObjectInput in) {
            // Does not matter
        }
    }

    @Test
    public void TestImmutableWriteExternal() {
        Assert.assertFalse(MutableClasses.mutableSignature(
                "Ledu/umd/cs/findbugs/util/MutableClassesTest$ImmutableWriteExternal;"));
    }
}
