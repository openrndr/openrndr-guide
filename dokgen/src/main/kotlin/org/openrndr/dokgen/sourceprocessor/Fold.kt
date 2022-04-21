package org.openrndr.dokgen.sourceprocessor

import kastree.ast.Node


interface Folder<A> {
    val pre: (A, Node) -> A
    val post: ((A, Node) -> A)?
}

fun <A> makeFoldFunction(acc: A, folder: Folder<A>): (Iterable<Node?>) -> A {
    return { nodes ->
        nodes.filterNotNull()
            .fold(acc) { _acc, node ->
                node.fold(_acc, folder)
            }
    }
}

fun <A> Iterable<Node?>.fold(acc: A, folder: Folder<A>): A =
    makeFoldFunction(acc, folder)(this)

fun <A> Node.fold(acc: A, folder: Folder<A>): A {
    val newAcc = folder.pre(acc, this)
    val foldNodes = makeFoldFunction(newAcc, folder)
    val result = when (this) {
        is Node.File -> {
            foldNodes((anns + pkg + imports + decls))
        }
        is Node.Script -> {
            foldNodes((anns + pkg + imports + exprs))
        }
        is Node.Package -> {
            foldNodes(mods)
        }
        is Node.Import -> {
            newAcc
        }
        is Node.Decl.Structured -> {
            foldNodes((mods + typeParams + primaryConstructor + parentAnns + parents + typeConstraints + members))
        }
        is Node.Decl.Init -> {
            foldNodes(listOf(block))
        }
        is Node.Decl.Func -> {
            foldNodes((mods + typeParams + receiverType + paramTypeParams + params + type + typeConstraints + body))
        }
        is Node.Decl.Property -> {
            foldNodes((mods + typeParams + receiverType + vars + typeConstraints + expr + accessors))
        }
        is Node.Decl.TypeAlias -> {
            foldNodes((mods + typeParams + type))
        }
        is Node.Decl.Constructor -> {
            foldNodes(mods + params + delegationCall + block)
        }
        is Node.Decl.EnumEntry -> {
            foldNodes(mods + args + members)
        }
        is Node.Decl.Structured.Parent.CallConstructor -> {
            foldNodes(typeArgs + type + args + lambda)
        }
        is Node.Decl.Structured.Parent.Type -> {
            foldNodes(listOf(type, by))
        }
        is Node.Decl.Structured.PrimaryConstructor -> {
            foldNodes(mods + params)
        }
        is Node.Decl.Func.Param -> {
            foldNodes(mods + type + default)
        }
        is Node.Decl.Func.Body.Block -> {
            foldNodes(listOf(block))
        }
        is Node.Decl.Func.Body.Expr -> {
            foldNodes(listOf(expr))
        }
        is Node.Decl.Property.Var -> {
            foldNodes(listOf(type))
        }
        is Node.Decl.Property.Accessors -> {
            foldNodes(listOf(first, second))
        }
        is Node.Decl.Property.Accessor.Get -> {
            foldNodes(mods + type + body)
        }
        is Node.Decl.Property.Accessor.Set -> {
            foldNodes(mods + paramMods + paramType + body)
        }
        is Node.Decl.Constructor.DelegationCall -> {
            foldNodes(args)
        }
        is Node.TypeParam -> {
            foldNodes(mods + type)
        }
        is Node.TypeConstraint -> {
            foldNodes(anns + type)
        }
        is Node.TypeRef.Paren -> {
            foldNodes(mods + type)
        }
        is Node.TypeRef.Func -> {
            foldNodes(params + receiverType + type)
        }
        is Node.TypeRef.Simple -> {
            foldNodes(pieces)
        }
        is Node.TypeRef.Nullable -> {
            foldNodes(listOf(type))
        }
        is Node.TypeRef.Dynamic -> {
            newAcc
        }
        is Node.TypeRef.Func.Param -> {
            foldNodes(listOf(type))
        }
        is Node.TypeRef.Simple.Piece -> {
            foldNodes(typeParams)
        }
        is Node.Type -> {
            foldNodes(mods + ref)
        }
        is Node.ValueArg -> {
            foldNodes(listOf(expr))
        }
        is Node.Expr.If -> {
            foldNodes(listOf(expr, body, elseBody))
        }
        is Node.Expr.Try -> {
            foldNodes(catches + block + finallyBlock)
        }
        is Node.Expr.For -> {
            foldNodes(anns + vars + inExpr + body)
        }
        is Node.Expr.While -> {
            foldNodes(listOf(expr, body))
        }
        is Node.Expr.BinaryOp -> {
            foldNodes(listOf(lhs, oper, rhs))
        }
        is Node.Expr.UnaryOp -> {
            foldNodes(listOf(expr, oper))
        }
        is Node.Expr.TypeOp -> {
            newAcc
        }
        is Node.Expr.DoubleColonRef.Callable -> {
            foldNodes(listOf(recv))
        }
        is Node.Expr.DoubleColonRef.Class -> {
            foldNodes(listOf(recv))
        }
        is Node.Expr.Paren -> {
            foldNodes(listOf(expr))
        }
        is Node.Expr.StringTmpl -> {
            foldNodes(elems)
        }
        is Node.Expr.Const -> {
            newAcc
        }
        is Node.Expr.Brace -> {
            foldNodes(params + block)
        }
        is Node.Expr.Brace.Param -> {
            foldNodes(vars + destructType)
        }
        is Node.Expr.This -> {
            newAcc
        }
        is Node.Expr.Super -> {
            foldNodes(listOf(typeArg))
        }
        is Node.Expr.When -> {
            foldNodes(entries + expr)
        }
        is Node.Expr.Object -> {
            foldNodes(parents + members)
        }
        is Node.Expr.Throw -> {
            foldNodes(listOf(expr))
        }
        is Node.Expr.Return -> {
            foldNodes(listOf(expr))
        }
        is Node.Expr.Continue -> {
            newAcc
        }
        is Node.Expr.Break -> {
            newAcc
        }
        is Node.Expr.CollLit -> {
            foldNodes(exprs)
        }
        is Node.Expr.Name -> {
            newAcc
        }
        is Node.Expr.Labeled -> {
            foldNodes(listOf(expr))
        }
        is Node.Expr.Annotated -> {
            foldNodes(anns + expr)
        }
        is Node.Expr.Call -> {
            foldNodes(emptyList<Node>() + expr + typeArgs + args + lambda)
        }
        is Node.Expr.ArrayAccess -> {
            foldNodes(emptyList<Node>() + expr + indices)
        }
        is Node.Expr.AnonFunc -> {
            foldNodes(listOf(func))
        }
        is Node.Expr.Property -> {
            foldNodes(listOf(decl))
        }
        is Node.Expr.BinaryOp.Oper.Infix -> {
            newAcc
        }
        is Node.Expr.BinaryOp.Oper.Token -> {
            newAcc
        }
        is Node.Expr.UnaryOp.Oper -> {
            newAcc
        }
        is Node.Expr.TypeOp.Oper -> {
            newAcc
        }
        is Node.Expr.DoubleColonRef.Recv.Expr -> {
            foldNodes(listOf(expr))
        }
        is Node.Expr.DoubleColonRef.Recv.Type -> {
            foldNodes(listOf(type))
        }
        is Node.Expr.StringTmpl.Elem.Regular -> {
            newAcc
        }
        is Node.Expr.StringTmpl.Elem.ShortTmpl -> {
            newAcc
        }
        is Node.Expr.StringTmpl.Elem.UnicodeEsc -> {
            newAcc
        }
        is Node.Expr.StringTmpl.Elem.RegularEsc -> {
            newAcc
        }
        is Node.Expr.StringTmpl.Elem.LongTmpl -> {
            newAcc
        }
        is Node.Expr.When.Entry -> {
            foldNodes(conds + body)
        }
        is Node.Expr.When.Cond.Expr -> {
            foldNodes(listOf(expr))
        }
        is Node.Expr.When.Cond.In -> {
            foldNodes(listOf(expr))
        }
        is Node.Expr.When.Cond.Is -> {
            foldNodes(listOf(type))
        }
        is Node.Expr.Call.TrailLambda -> {
            foldNodes(anns + func)
        }
        is Node.Block -> {
            foldNodes(stmts)
        }
        is Node.Stmt.Decl -> {
            foldNodes(listOf(decl))
        }
        is Node.Stmt.Expr -> {
            foldNodes(listOf(expr))
        }
        is Node.Modifier.AnnotationSet -> {
            foldNodes(anns)
        }
        is Node.Modifier.Lit -> {
            newAcc
        }
        is Node.Modifier.AnnotationSet.Annotation -> {
            foldNodes(typeArgs + args)
        }
        is Node.Extra.BlankLines -> {
            newAcc
        }
        is Node.Extra.Comment -> {
            newAcc
        }
        is Node.Expr.Try.Catch -> {
            foldNodes(anns + varType + block)
        }
    }
    return folder.post?.invoke(result, this) ?: result
}

