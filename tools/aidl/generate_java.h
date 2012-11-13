#ifndef GENERATE_JAVA_H
#define GENERATE_JAVA_H

#include "aidl_language.h"
<<<<<<< HEAD
#include "AST.h"
=======
>>>>>>> 54b6cfa... Initial Contribution

#include <string>

using namespace std;

int generate_java(const string& filename, const string& originalSrc,
                interface_type* iface);

<<<<<<< HEAD
Class* generate_binder_interface_class(const interface_type* iface);
Class* generate_rpc_interface_class(const interface_type* iface);

string gather_comments(extra_text_type* extra);
string append(const char* a, const char* b);

class VariableFactory
{
public:
    VariableFactory(const string& base); // base must be short
    Variable* Get(Type* type);
    Variable* Get(int index);
private:
    vector<Variable*> m_vars;
    string m_base;
    int m_index;
};

=======
>>>>>>> 54b6cfa... Initial Contribution
#endif // GENERATE_JAVA_H

