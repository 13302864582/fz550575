// IEQAidlInterface.aidl
package eq.tools.equlizer_globle;

// Declare any non-default types here with import statements

interface IEQAidlInterface {
    void toast();
    void setTotalEnable(boolean checkState);
    void setBandLevel(int band, float dbValue);
    void setVirtualLevel(int strengh);
    void setBassLevel(int strengh);
    boolean isEQEnable();
    boolean isBassEnable();
    boolean isVirtualEnable();
    void createNotify(int bass,int virtual);
    void deleteNotify();
    boolean isTotalEnable();
}
